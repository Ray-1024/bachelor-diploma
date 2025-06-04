<script setup lang="ts">
import {marked} from 'marked';
import DOMPurify from 'dompurify';
import {computed, nextTick, onMounted, ref} from 'vue';
import {ArticleDto, TagsDto} from "@/client/data";
import {client} from "@/client/client";
import TagPicker from "@/components/tag/TagPicker.vue";

const props = defineProps<{
  article: ArticleDto
}>();

const statusMessage = ref('');
const editor = ref<HTMLTextAreaElement | null>(null);
const fileInput = ref<HTMLInputElement | null>(null);

marked.setOptions({
  breaks: true,
  gfm: true,
  smartypants: true
});

const compiledMarkdown = computed(() => {
  return DOMPurify.sanitize(marked(props.article.article));
});

const insertText = (textToInsert: string) => {
  if (!editor.value) return;

  const textarea = editor.value;
  const startPos = textarea.selectionStart;
  const endPos = textarea.selectionEnd;
  const selectedText = props.article.article.substring(startPos, endPos);

  const wrappedText = textToInsert.includes('selected')
      ? textToInsert.replace('selected', selectedText)
      : selectedText
          ? textToInsert + '\n' + selectedText
          : textToInsert;

  props.article.article =
      props.article.article.substring(0, startPos) +
      wrappedText +
      props.article.article.substring(endPos);

  let newCursorPos = startPos + wrappedText.length;
  if (selectedText && !textToInsert.includes('selected')) {
    newCursorPos = startPos + textToInsert.length + 1;
  }

  nextTick(() => {
    textarea.setSelectionRange(newCursorPos, newCursorPos);
    textarea.focus();
  });
};

async function handleTagsUpdate(params: TagsDto) {
  props.article.tags = params.tags.map(t => t.id);
}

async function handlePaste(event: ClipboardEvent) {
  const items = (event.clipboardData || (event as any).originalEvent.clipboardData).items;

  for (let i = 0; i < items.length; i++) {
    if (items[i].type.indexOf('image') !== -1) {
      event.preventDefault();
      const blob = items[i].getAsFile();
      if (blob) {
        const url = client.images.save(blob);
        insertText(`![alt](${url})`);
      }
      return;
    }
  }
}

const triggerFileUpload = () => {
  fileInput.value?.click();
};

const handleFileUpload = async (event: Event) => {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0];
  if (file && file.type.match('image.*')) {
    const url = client.images.save(file);
    insertText(`![alt](${url})`);
  }
  input.value = '';
};

const saveArticle = () => {
  client.articles.update(props.article.id, {
    title: props.article.title,
    article: props.article.article,
    tags: props.article.tags
  });
  showStatus('Article saved successfully!');
};

const showStatus = (message: string) => {
  statusMessage.value = message;
  setTimeout(() => statusMessage.value = '', 3000);
};

onMounted(() => {
  editor.value?.focus();
});
</script>

<template>
  <div class="markdown-editor">
    <div class="editor-header">
      <h2>Article Editor</h2>
      <div class="toolbar">Title:<input v-model="props.article.title" class="article-title"></div>
      <div class="toolbar">
        <button @click="insertText('**bold**')" title="Bold">B</button>
        <button @click="insertText('*italic*')" title="Italic">I</button>
        <button @click="insertText('[link](url)')" title="Link">Link</button>
        <button @click="insertText('![alt](url)')" title="Image">Image Link</button>
        <button @click="insertText('# Heading')" title="Heading 1">H1</button>
        <button @click="insertText('## Heading')" title="Heading 2">H2</button>
        <button @click="insertText('- list item')" title="List">List</button>
        <button @click="insertText('```\ncode\n```')" title="Code Block">Code</button>
        <input
            type="file"
            id="file-upload"
            ref="fileInput"
            @change="handleFileUpload"
            accept="image/*"
            style="display: none"
        />
        <button @click="triggerFileUpload" title="Upload Image">Upload Image</button>
      </div>
    </div>

    <div class="editor-container">
      <textarea
          ref="editor"
          v-model="props.article.article"
          @paste="handlePaste"
          @keydown.ctrl.83.prevent="saveArticle"
          placeholder="Write your article in Markdown here..."
          class="editor-textarea"
          spellcheck="false"
      ></textarea>

      <div class="preview-container">
        <div class="preview-header">
          <h3>Preview</h3>
        </div>
        <div class="markdown-preview" v-html="compiledMarkdown"></div>
      </div>
    </div>

    <div class="editor-footer">
      <div class="toolbar">
        <TagPicker :tags="client.tags.cache" @tags-update="handleTagsUpdate"
                   :selected-tags="props.article.tags.map(id => client.tags.getById(id))"/>
      </div>
      <button @click="saveArticle" class="save-button">Save Article</button>
      <span class="status-message">{{ statusMessage }}</span>
    </div>
  </div>
</template>

<style scoped>
.markdown-editor {
  display: flex;
  flex-direction: column;
  /*  height: 100%;*/
  max-width: 1200px;
  margin: 0 auto;
  border: 1px solid #e1e4e8;
  border-radius: 8px;
  overflow: hidden;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.editor-header {
  padding: 12px 15px;
  background-color: #f6f8fa;
  border-bottom: 1px solid #e1e4e8;
}

.editor-header h2 {
  margin: 0 0 12px 0;
  color: #24292e;
  font-size: 1.25rem;
}

.toolbar {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.article-title {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
  font-size: 14px;
}

.toolbar button {
  padding: 6px 10px;
  background-color: #fff;
  border: 1px solid #d1d5da;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
}

.toolbar button:hover {
  background-color: #f3f4f6;
  border-color: #c9ccd1;
}

.editor-container {
  display: flex;
  height: 70vh;
  min-height: 400px;
}

.editor-textarea {
  flex: 1;
  padding: 16px;
  border: none;
  border-right: 1px solid #e1e4e8;
  resize: none;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 14px;
  line-height: 1.6;
  color: #24292e;
  background-color: #fafbfc;
  tab-size: 2;
}

.editor-textarea:focus {
  outline: none;
  background-color: #fff;
}

.preview-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.preview-header {
  padding: 12px 16px;
  border-bottom: 1px solid #e1e4e8;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-header h3 {
  margin: 0;
  color: #24292e;
  font-size: 1rem;
}

.copy-button {
  padding: 4px 8px;
  background-color: #f6f8fa;
  border: 1px solid #d1d5da;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.copy-button:hover {
  background-color: #e1e4e8;
}

.markdown-preview {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  color: #24292e;
  line-height: 1.6;
}

.markdown-preview >>> h1,
.markdown-preview >>> h2,
.markdown-preview >>> h3 {
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
}

.markdown-preview >>> h1 {
  font-size: 2em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.3em;
}

.markdown-preview >>> h2 {
  font-size: 1.5em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.3em;
}

.markdown-preview >>> pre {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 16px;
  overflow: auto;
}

.markdown-preview >>> code {
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  background-color: rgba(27, 31, 35, 0.05);
  border-radius: 3px;
  padding: 0.2em 0.4em;
  font-size: 85%;
}

.markdown-preview >>> img {
  max-width: 100%;
  border-radius: 4px;
}

.editor-footer {
  padding: 12px 16px;
  background-color: #f6f8fa;
  border-top: 1px solid #e1e4e8;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.save-button {
  padding: 8px 16px;
  background-color: #2ea44f;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s;
}

.save-button:hover {
  background-color: #2c974b;
}

.status-message {
  color: #586069;
  font-size: 14px;
  flex-grow: 1;
  text-align: center;
}

.character-count {
  color: #959da5;
  font-size: 13px;
  font-family: monospace;
}
</style>