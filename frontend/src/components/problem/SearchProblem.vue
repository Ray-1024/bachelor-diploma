<script setup lang="ts">
import {client} from "@/client/client";
import SearchWithTags from "@/components/search/SearchWithTags.vue";
import {onMounted, ref} from "vue";
import ProblemCardList from "@/components/problem/ProblemCardList.vue";
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
  client.problems.getAll(1, props.pageSize, props.searchText, props.searchTags);
  client.tags.getAll(1, 100);
});


const handleSearch = async (params: SearchParams) => {
  searchText.value = params.text;
  searchTags.value = params.tags;
  client.articles.getAll(1, props.pageSize, params.text, params.tags);
};

const emit = defineEmits(['searchArticle']);

async function handleSearchArticle(params: TagsDto) {
  emit('searchArticle', params);
}

const handlePageChange = async (params: PageDto) => {
  client.problems.getAll(params.page, props.pageSize, searchText.value, searchTags.value);
}

</script>

<template>
  <div class="problem-search">

    <div>
      <SearchWithTags :tags="client.tags.cache" @search="handleSearch" :search-text="props.searchText"
                      :search-tags="props.searchTags"/>
    </div>
    <div class="problem-search">
      <Pagination :current-page="1" :total-pages="100" @page-change="handlePageChange"/>
    </div>
    <div>
      <ProblemCardList :problems="client.problems.cache" @search-article="handleSearchArticle" :editable="false"/>
    </div>
  </div>
</template>

<style scoped>
.problem-search {
  margin: 0 auto;
  font-family: Arial, sans-serif;
}
</style>