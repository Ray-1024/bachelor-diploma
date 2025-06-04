<template>
  <div class="article-search">
    <div>
      <SearchWithTags :tags="client.tags.cache" @search="handleSearch" :search-text="props.searchText"
                      :search-tags="props.searchTags"/>
    </div>
    <div class="article-search">
      <Pagination :current-page="1" :total-pages="100" @page-change="handlePageChange"/>
    </div>
    <div>
      <ArticleCardList :articles="client.articles.cache" :editable="false" @search-problem="handleSearchProblem"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {client} from '@/client/client';
import SearchWithTags from "@/components/search/SearchWithTags.vue";
import ArticleCardList from "@/components/article/ArticleCardList.vue";
import {onMounted, ref} from "vue";
import {PageDto, SearchParams, TagDto, TagsDto} from "@/client/data";
import Pagination from "@/components/pagination/Pagination.vue";

const props = defineProps<{
  pageSize: Number,
  searchText: String,
  searchTags: TagDto[]
}>();

const searchText = ref('');
const searchTags = ref([]);

onMounted(() => {
  client.tags.getAll(1, 100);
  client.articles.getAll(1, props.pageSize, props.searchText, props.searchTags);
});

const handleSearch = async (params: SearchParams) => {
  searchText.value = params.text.toString();
  searchTags.value = params.tags;
  client.articles.getAll(1, props.pageSize, params.text, params.tags);
};

const handlePageChange = async (params: PageDto) => {
  client.articles.getAll(params.page, props.pageSize, searchText.value, searchTags.value);
}

const emit = defineEmits(['searchProblem']);

async function handleSearchProblem(params: TagsDto) {
  emit('searchProblem', params);
}

</script>

<style scoped>
.article-search {
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

</style>