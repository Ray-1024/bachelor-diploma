<template>
  <Overlay>
    <template v-slot:card>
      <div class="submission-header">
        <span class="submission-title">{{ client.problems.getById(props.submission.problemId).title }}</span>
        <span class="submission-language">{{ props.submission.language }}</span>
        <span class="submission-date">{{ formatDate(props.submission.creationTime) }}</span>
        <span class="submission-language">{{ props.submission.status }}</span>
      </div>
      <div class="submission-code">
        {{ truncateText(props.submission.sourceCode, 50) }}
      </div>
    </template>
    <template v-slot:popup>
      <span class="submission-title">{{ client.problems.getById(props.submission.problemId).title }}</span>
      <span class="submission-language">{{ props.submission.language }}</span>
      <span class="submission-date">{{ formatDate(props.submission.creationTime) }}</span>
      <span class="submission-language">{{ props.submission.status }}</span>
      <div class="submission-code">
        <textarea
            :value="props.submission.sourceCode.toString()"
            placeholder="Enter your code here..."
            spellcheck="false"
            :readonly="true"
        ></textarea>
      </div>
    </template>
  </Overlay>
</template>

<script setup lang="ts">
import {client} from "@/client/client";
import {SubmissionDto} from "@/client/data"
import Overlay from "@/components/overlay/Overlay.vue";

const props = defineProps<{
  submission: SubmissionDto
}>();

function formatDate(dateString: Date) {
  const options: Intl.DateTimeFormatOptions = {year: 'numeric', month: 'short', day: 'numeric'};
  return dateString.toLocaleDateString(undefined, options);
}

function truncateText(text: String, maxLength: number): String {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength - 3) + '...';
}
</script>

<style scoped>
span{
  margin-left: 3px;
  margin-right: 3px;
}
textarea {
  width: 100%;
  height: 300px;
  font-family: 'Consolas', 'Monaco', monospace;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 15px;
}

.submission-header {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: 8px;
  gap: 10px;
}

.submission-title {
  font-weight: bold;
  font-size: 1.1em;
  flex: 1 1 100%;
}

.submission-language {
  color: #555;
  font-size: 0.9em;
}

.submission-date {
  color: #777;
  font-size: 0.85em;
}

.submission-code {
  color: #444;
  margin: 10px 0;
  line-height: 1.5;
}
</style>