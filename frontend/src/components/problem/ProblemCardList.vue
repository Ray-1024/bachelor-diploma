<script setup lang="ts">

import {ProblemDto, TagsDto} from "@/client/data";
import ProblemCard from "@/components/problem/ProblemCard.vue";
import Overlay from "@/components/overlay/Overlay.vue";
import CreateProblem from "@/components/problem/CreateProblem.vue";
import UpdateProblem from "@/components/problem/UpdateProblem.vue";
import {client} from "@/client/client";

const props = defineProps<{
  problems: ProblemDto[],
  editable: boolean
}>();

const emit = defineEmits(['searchArticle']);

async function handleSearchArticle(params: TagsDto) {
  emit('searchArticle', params);
}

const deleteProblem = async (problem: ProblemDto) => {
  client.problems.deleteById(problem.id);
  const idx = props.problems.indexOf(problem);
  props.problems.splice(idx, 1);
}
</script>

<template>
  <div v-if="props.editable" class="section-header">
    <Overlay>
      <template v-slot:card>
        <button class="create-btn">+ New Problem</button>
      </template>
      <template v-slot:popup>
        <CreateProblem/>
      </template>
    </Overlay>

  </div>
  <div v-if="props.problems.length" class="problems-list">
    <div v-for="(problem,index) in props.problems" :key="index" class="problem-card">
      <div class="problem-info">
        <ProblemCard :problem="problem" @search-article="handleSearchArticle"/>
      </div>
      <div class="problem-actions" v-if="props.editable">
        <Overlay>
          <template v-slot:card>
            <button @click="" class="edit-btn">Edit</button>
          </template>
          <template v-slot:popup>
            <UpdateProblem :problem="problem"/>
          </template>
        </Overlay>
        <button @click="deleteProblem(problem)" class="delete-btn">Delete</button>
      </div>
    </div>
  </div>
  <p v-else class="no-content">No problems.</p>
</template>

<style scoped>
.section-header {
  display: flex;
  justify-content: right;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  margin: 0;
}

.create-btn {
  background-color: #2ecc71;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.create-btn:hover {
  background-color: #27ae60;
}

.articles-list,
.problems-list,
.submissions-list {
  display: grid;
  gap: 15px;
}

.article-card,
.problem-card,
.submission-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-info h3,
.problem-info h3,
.submission-info h3 {
  margin: 0 0 10px 0;
}

.article-info h3 a,
.problem-info h3 a,
.submission-info h3 a {
  color: #3498db;
  text-decoration: none;
}

.article-info h3 a:hover,
.problem-info h3 a:hover,
.submission-info h3 a:hover {
  text-decoration: underline;
}

.article-actions,
.problem-actions,
.submission-actions {
  display: flex;
  gap: 10px;
}

.edit-btn {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.delete-btn {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.no-content {
  color: #666;
  font-style: italic;
  text-align: center;
  padding: 40px 0;
}

.article-actions,
.problem-actions,
.submission-actions {
  display: flex;
  gap: 10px;
}

.results-container {
  background: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}


.close-btn {
  background-color: #95a5a6;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.confirm-delete-btn {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.article-info h3,
.problem-info h3,
.submission-info h3 {
  margin: 0 0 10px 0;
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

.edit-btn {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.delete-btn {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}
</style>