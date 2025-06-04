<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {client} from '@/client/client'
import SearchArticle from '@/components/article/SearchArticle.vue'
import SearchProblem from '@/components/problem/SearchProblem.vue'
import Authentication from '@/components/authentication/Authentication.vue'
import Profile from '@/components/profile/Profile.vue'
import {TagsDto} from "@/client/data";

onMounted(() => {
  client.tags.getAll(1, 100);
  client.articles.getAll(1, 100, '', []);
  client.articles.getAllMy(1, 100, '', []);
  client.problems.getAll(1, 100, '', []);
  client.problems.getAllMy(1, 100, '', []);
  client.submissions.getAllMy(1, 100);
});


const tabs = reactive([
  'Search Articles', 'Search Problems', 'Profile'
]);
const selectedTab = ref(tabs[0]);
const searchText = ref('');
const searchTags = ref([]);

async function handleSearchArticle(params: TagsDto) {
  searchText.value = '';
  searchTags.value = params.tags;
  selectedTab.value = tabs[0];
}

async function handleSearchProblem(params: TagsDto) {
  searchText.value = '';
  searchTags.value = params.tags;
  selectedTab.value = tabs[1];
}


</script>

<template>
  <div class="main-page" v-if="!client.auth.token">
    <Authentication/>
  </div>
  <div class="main-page" v-if="client.auth.token">
    <div class="tabs">
      <button
          v-for="tab in tabs"
          :key="tab.toString()"
          @click="selectedTab = tab"
          :class="{ 'active': selectedTab === tab }"
      >
        {{ tab }}
      </button>
    </div>
    <div class="tab-content">
      <div v-if="selectedTab === tabs[0]">
        <SearchArticle :page-size="10" :search-text="searchText" :search-tags="searchTags"
                       @search-problem="handleSearchProblem"/>
      </div>
      <div v-if="selectedTab === tabs[1]">
        <SearchProblem :page-size="10" :search-text="searchText" :search-tags="searchTags"
                       @search-article="handleSearchArticle"/>
      </div>
      <div v-if="selectedTab === tabs[2]">
        <Profile :page-size="10" @search-article="handleSearchArticle" @search-problem="handleSearchProblem"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-page {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.tabs {
  display: flex;
  border-bottom: 1px solid #ddd;
  margin-bottom: 20px;
}

.tabs button {
  padding: 10px 20px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
  color: #555;
  transition: all 0.3s ease;
  border-bottom: 3px solid transparent;
}

.tabs button:hover {
  color: #333;
  background-color: #f5f5f5;
}

.tabs button.active {
  color: #2c3e50;
  border-bottom: 3px solid #42b983;
  font-weight: bold;
}

.tab-content {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.container > *:first-child {
  position: relative;
  z-index: 1;
}
</style>