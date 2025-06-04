<script setup lang="ts">
import {client} from "@/client/client";
import {onMounted, reactive, ref} from "vue";
import ArticleCardList from "@/components/article/ArticleCardList.vue";
import ProblemCardList from "@/components/problem/ProblemCardList.vue";
import SubmissionCardList from "@/components/submission/SubmissionCardList.vue";
import {TagsDto} from "@/client/data";

const props = defineProps<{
  pageSize: Number
}>();

const emit = defineEmits(['searchArticle', 'searchProblem']);

async function handleSearchArticle(params: TagsDto) {
  emit('searchArticle', params);
}

async function handleSearchProblem(params: TagsDto) {
  emit('searchProblem', params);
}

onMounted(() => {
  client.articles.getAllMy(1, props.pageSize, '', []);
  client.problems.getAllMy(1, props.pageSize, '', []);
  client.submissions.getAllMy(1, props.pageSize);
});


const tabs = reactive([
  {id: 'articles', label: 'Articles'},
  {id: 'problems', label: 'Problems'},
  {id: 'submissions', label: 'Submissions'}
]);
const activeTab = ref(tabs[0]);
</script>

<template>
  <div class="user-profile">
    <div class="profile-header">
      <div class="user-info">
        <h1>{{ client.auth.username }}</h1>
        <div class="stats">
          <div class="stat">
            <span class="stat-number">{{ client.articles.myCache.length }}</span>
            <span class="stat-label">Articles</span>
          </div>
          <div class="stat">
            <span class="stat-number">{{ client.problems.myCache.length }}</span>
            <span class="stat-label">Problems</span>
          </div>
          <div class="stat">
            <span class="stat-number">{{ client.submissions.myCache.length }}</span>
            <span class="stat-label">Submissions</span>
          </div>
        </div>
      </div>
    </div>

    <div class="content-tabs">
      <button
          v-for="tab in tabs"
          :key="tab.id"
          @click="activeTab = tab"
          :class="{ 'active': activeTab === tab }"
      >
        {{ tab.label }}
      </button>
    </div>

    <div v-show="activeTab === tabs[0]" class="tab-content">
      <div class="section-header">
        <h2>Created Articles</h2>
      </div>
      <ArticleCardList :articles="client.articles.myCache" :editable="true" @search-problem="handleSearchProblem"/>
    </div>

    <div v-show="activeTab === tabs[1]" class="tab-content">
      <div class="section-header">
        <h2>Created Problems</h2>
      </div>
      <ProblemCardList :problems="client.problems.myCache" :editable="true" @search-article="handleSearchArticle"/>
    </div>

    <div v-show="activeTab === tabs[2]" class="tab-content">
      <div class="section-header">
        <h2>Recent Submissions</h2>
      </div>
      <SubmissionCardList :submissions="client.submissions.myCache"/>
    </div>
  </div>
</template>

<style scoped>
.user-profile {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.profile-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
  align-items: flex-start;
}

.user-info {
  flex: 1;
}

.user-info h1 {
  margin: 0;
  font-size: 2rem;
}

.stats {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.stat {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 1.5rem;
  font-weight: bold;
}

.stat-label {
  font-size: 0.9rem;
  color: #666;
}

.content-tabs {
  display: flex;
  border-bottom: 1px solid #ddd;
  margin-bottom: 20px;
}

.content-tabs button {
  padding: 10px 20px;
  background: none;
  border: none;
  border-bottom: 3px solid transparent;
  cursor: pointer;
  font-size: 1rem;
  color: #666;
}

.content-tabs button.active {
  color: #3498db;
  border-bottom-color: #3498db;
  font-weight: bold;
}

.tab-content {
  padding: 10px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 30px;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
}

.modal h3 {
  margin-top: 0;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
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
</style>