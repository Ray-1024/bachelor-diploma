<script setup lang="ts">

import {SubmissionDto} from "@/client/data";
import SubmissionCard from "@/components/submission/SubmissionCard.vue";

const props = defineProps<{
  submissions: SubmissionDto[]
}>();

</script>

<template>
  <div class="results-container">
    <div v-if="props.submissions.length === 0" class="no-results">
      No articles found.
    </div>
    <ul v-else class="article-list">
      <li v-for="submission in props.submissions" :key="submission.id" class="article-item">
        <SubmissionCard :submission="submission"/>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.article-actions {
  display: flex;
  gap: 10px;
}

.results-container {
  background: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.no-results {
  text-align: center;
  padding: 20px;
  color: #666;
}

.article-list {
  list-style: none;
  padding: 0;
}

.article-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.article-item:last-child {
  border-bottom: none;
}
</style>