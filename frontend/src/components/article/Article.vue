<template>
  <div class="markdown-reader">
    <div class="submission-header">
      <h1>{{ props.article.title }}</h1>
      <div class="article-meta">
        <span class="author">{{ props.article.authorUsername }}</span>
        <span class="date">{{ formatDate(props.article.creationDate) }}</span>
      </div>
      <div>
        <Tag v-for="tag in props.article.tags" @click="emitSearchProblem({tags:[client.tags.getById(tag)]})">
          {{ client.tags.getById(tag).tag }}
        </Tag>
      </div>
    </div>
    <div class="markdown-content" v-html="compiledMarkdown()"></div>
  </div>
</template>

<script setup lang="ts">
import {marked} from 'marked';
import DOMPurify from 'dompurify';
import {ArticleDto, TagsDto} from "@/client/data";
import {client} from "@/client/client";
import Tag from "@/components/tag/Tag.vue";

const props = defineProps<{
  article: ArticleDto
}>();

const emit = defineEmits(['searchProblem']);

function emitSearchProblem(params: TagsDto) {
  emit('searchProblem', params);
}

function compiledMarkdown() {
  marked.setOptions({
    gfm: true,
    breaks: true,
    smartypants: true
  });
  return DOMPurify.sanitize(marked(props.article.article));
}

function formatDate(dateString: Date) {
  const options: Intl.DateTimeFormatOptions = {year: 'numeric', month: 'short', day: 'numeric'};
  return dateString.toLocaleDateString(undefined, options);
}
</script>

<style scoped>
.markdown-reader {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 1rem;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  line-height: 1.6;
  color: #333;
}

.submission-header {
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eaecef;
}

.submission-header h1 {
  margin: 0 0 0.5rem 0;
  font-size: 2.5rem;
  font-weight: 600;
  color: #24292e;
}

.article-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.9rem;
  color: #586069;
}

.author {
  font-weight: 500;
}

.date {
  opacity: 0.8;
}

.markdown-content {
  font-size: 1.1rem;
  line-height: 1.8;
}

.markdown-content >>> h1,
.markdown-content >>> h2,
.markdown-content >>> h3,
.markdown-content >>> h4,
.markdown-content >>> h5,
.markdown-content >>> h6 {
  margin-top: 1.5em;
  margin-bottom: 1em;
  font-weight: 600;
  color: #24292e;
}

.markdown-content >>> h1 {
  font-size: 2em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.3em;
}

.markdown-content >>> h2 {
  font-size: 1.5em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.3em;
}

.markdown-content >>> p {
  margin-bottom: 1em;
}

.markdown-content >>> a {
  color: #0366d6;
  text-decoration: none;
}

.markdown-content >>> a:hover {
  text-decoration: underline;
}

.markdown-content >>> img {
  max-width: 100%;
  height: auto;
  border-radius: 6px;
  margin: 1em 0;
}

.markdown-content >>> pre {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 1em;
  overflow: auto;
  margin: 1em 0;
}

.markdown-content >>> code {
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  background-color: rgba(27, 31, 35, 0.05);
  border-radius: 3px;
  padding: 0.2em 0.4em;
  font-size: 85%;
}

.markdown-content >>> blockquote {
  border-left: 4px solid #dfe2e5;
  color: #6a737d;
  padding: 0 1em;
  margin: 0 0 1em 0;
}

.markdown-content >>> ul,
.markdown-content >>> ol {
  margin-bottom: 1em;
  padding-left: 2em;
}

.markdown-content >>> li {
  margin-bottom: 0.5em;
}

.markdown-content >>> table {
  display: block;
  width: 100%;
  overflow: auto;
  margin: 1em 0;
  border-collapse: collapse;
}

.markdown-content >>> table th {
  font-weight: 600;
  background-color: #f6f8fa;
}

.markdown-content >>> table th,
.markdown-content >>> table td {
  padding: 0.5em 1em;
  border: 1px solid #dfe2e5;
}
</style>