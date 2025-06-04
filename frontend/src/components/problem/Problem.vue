<template>
  <div class="problem-page">
    <div class="problem-header">
      <h1>{{ props.problem.title }}</h1>
      <div class="problem-meta">
        <span class="time-limit">Time limit: {{ props.problem.timeLimitMilliseconds.valueOf() / 1000 }} sec</span>
        <span class="memory-limit">Memory limit: {{ props.problem.memoryLimitBytes.valueOf() / 1000000 }} MB</span>
        <div class="problem-meta">
          <Tag v-for="(tag,index) in props.problem.tags" :key="index"
               @click="emitSearchArticle({tags:[client.tags.getById(tag)]})">
            {{ client.tags.getById(tag).tag }}
          </Tag>
        </div>
      </div>
    </div>

    <div class="problem-content">
      <div class="problem-statement">
        <div>{{ props.problem.description }}</div>

        <div class="input-specification">
          <h3>Input</h3>
          <div>{{ props.problem.input }}</div>
        </div>

        <div class="output-specification">
          <h3>Output</h3>
          <div v-html="props.problem.output"></div>
        </div>

        <div class="sample-tests">
          <h3>Examples</h3>
          <div v-for="(sample, index) in props.problem.samples" :key="index" class="sample-test">
            <div class="input">
              <h4>Input #{{ index + 1 }}</h4>
              <pre>{{ sample.input }}</pre>
            </div>
            <div class="output">
              <h4>Output #{{ index + 1 }}</h4>
              <pre>{{ sample.output }}</pre>
            </div>
          </div>
        </div>
      </div>

      <div class="code-submission">
        <div class="language-selector">
          <label for="language">Language:</label>
          <select id="language" v-model="selectedLanguage">
            <option v-for="lang in languages" :value="lang" :key="lang.value">
              {{ lang.name }}
            </option>
          </select>
        </div>

        <div class="code-editor">
          <textarea
              v-model="sourceCode"
              placeholder="Enter your code here..."
              spellcheck="false"
          ></textarea>
        </div>

        <div class="submission-controls">
          <button @click="submitCode" class="submit-button">Submit</button>
        </div>

        <div v-if="client.submissions.cache.length > 0" class="test-results">
          <h3>Test Results</h3>
          <div v-for="(submission,index) in client.submissions.cache" :key="index" class="submission-card">
            <div class="submission-info">
              <SubmissionCard :submission="submission"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {client} from "@/client/client";
import {onMounted, reactive, ref} from "vue";
import {ProblemDto, TagsDto} from "@/client/data";
import SubmissionCard from "@/components/submission/SubmissionCard.vue";
import Tag from "@/components/tag/Tag.vue";

const props = defineProps<{
  problem: ProblemDto
}>();

const emit = defineEmits(['searchArticle']);

function emitSearchArticle(params: TagsDto) {
  emit('searchArticle', params);
}

const languages = reactive([
  {name: 'C++', value: 'cpp'},
  {name: 'Java', value: 'java'}
]);
const selectedLanguage = ref(languages[0]);
const sourceCode = ref('');

function submitCode() {
  client.submissions.create(props.problem, {
    language: selectedLanguage.value,
    sourceCode: sourceCode.value.toString()
  });
  client.submissions.getAll(1, 100, props.problem);
}

onMounted(() => {
  client.submissions.getAll(1, 100, props.problem);
});
</script>

<style scoped>
.problem-page {
  margin: 0 auto;
  padding: 20px;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  color: #333;
}

.problem-header {
  margin-bottom: 30px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 15px;
}

.problem-header h1 {
  margin: 0;
  color: #2c3e50;
}

.problem-meta {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

.problem-meta span {
  margin-right: 20px;
}

.problem-content {
  display: flex;
  gap: 30px;
}

.problem-statement {
  flex: 2;
  line-height: 1.6;
}

.problem-statement h2, .problem-statement h3, .problem-statement h4 {
  color: #2c3e50;
  margin-top: 25px;
}

.problem-statement pre {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
}

.sample-test {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.sample-test .input, .sample-test .output {
  flex: 1;
}

.sample-test pre {
  margin: 5px 0 0 0;
}

.code-submission {
  flex: 1;
  min-width: 40%;
}

.language-selector {
  margin-bottom: 15px;
}

.language-selector select {
  padding: 5px;
  margin-left: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.code-editor textarea {
  width: 100%;
  height: 300px;
  font-family: 'Consolas', 'Monaco', monospace;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 15px;
}

.custom-input textarea {
  width: 100%;
  font-family: 'Consolas', 'Monaco', monospace;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 15px;
}

.submission-controls {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.submission-controls button {
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.submit-button {
  background-color: #2ecc71;
  color: white;
}

.submit-button:hover {
  background-color: #27ae60;
}

.test-results {
  padding: 15px;
  border-radius: 4px;
  margin-top: 20px;
}

.error-details pre {
  margin: 10px 0 0 0;
  white-space: pre-wrap;
}

.tag {
  display: inline-block;
  background: #e0e0e0;
  padding: 4px 8px;
  border-radius: 4px;
  margin-right: 5px;
  margin-bottom: 5px;
  font-size: 12px;
}

.submission-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.submission-info h3 {
  margin: 0 0 10px 0;
}
</style>