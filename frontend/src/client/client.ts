import axios from 'axios'
import {reactive} from 'vue'
import {
    ArticleDto,
    ArticleUpdateDto,
    ProblemDto,
    ProblemUpdateDto,
    SubmissionCreateDto,
    SubmissionDto,
    TagDto
} from '@/client/data'
import {samples} from "@/client/samples";

export interface BackendClient {
    api: {
        basePath: String,
        apiPath: String,
        authPath: String,
        articlesPath: String,
        articlesAuthorPath: String,
        problemsPath: String,
        problemsAuthorPath: String,
        tagsPath: String,
        submissionsAuthorPath: String,
        imagesPath: String
    },
    errors: Error[],
    auth: {
        username: String,
        password: String,
        token: String,
        signIn: (username: String, password: String) => void,
        signUp: (username: String, password: String) => void,
    },
    tags: {
        cache: TagDto[],
        getAll: (page: Number, size: Number) => void,
        getById: (id: Number) => TagDto
    },
    articles: {
        cache: ArticleDto[],
        myCache: ArticleDto[],
        getAll: (page: Number, size: Number, text: String, tags: TagDto[]) => void,
        getAllMy: (page: Number, size: Number, text: String, tags: TagDto[]) => void,
        create: (article: ArticleUpdateDto) => void,
        update: (id: Number, article: ArticleUpdateDto) => void,
        deleteById: (id: Number) => void
    },
    problems: {
        cache: ProblemDto[],
        myCache: ProblemDto[],
        getAll: (page: Number, size: Number, text: String, tags: TagDto[]) => void,
        getAllMy: (page: Number, size: Number, text: String, tags: TagDto[]) => void,
        getById: (id: Number) => ProblemDto,
        create: (problem: ProblemUpdateDto) => void,
        update: (id: Number, problem: ProblemUpdateDto) => void,
        deleteById: (id: Number) => void
    },
    submissions: {
        cache: SubmissionDto[],
        myCache: SubmissionDto[],
        getAll: (page: Number, size: Number, problem: ProblemDto) => void,
        getAllMy: (page: Number, size: Number) => void,
        create: (problem: ProblemDto, submission: SubmissionCreateDto) => void
    },
    images: {
        get: (name: String) => Blob,
        save: (data: Blob) => String
    }
}

export const client = reactive({
    api: {
        basePath: 'http://localhost:8080',
        apiPath: 'http://localhost:8080/api',
        authPath: 'http://localhost:8080/api/auth',
        articlesPath: 'http://localhost:8080/api/articles',
        articlesAuthorPath: 'http://localhost:8080/api/articles/author',
        problemsPath: 'http://localhost:8080/api/problems',
        problemsAuthorPath: 'http://localhost:8080/api/problems/author',
        tagsPath: 'http://localhost:8080/api/tags',
        submissionsAuthorPath: 'http://localhost:8080/api/submissions/author',
        imagesPath: 'http://localhost:8080/api/images'
    },
    errors: [],
    auth: {
        username: undefined,
        password: undefined,
        token: undefined,
        signIn: (username: String, password: String): void => {
            axios.put(client.api.authPath.toString(),
                {
                    username: username,
                    password: password
                }
            ).then(result => {
                client.auth.username = username;
                client.auth.password = password;
                client.auth.token = result.data.token;
            }).catch(error => {
                client.errors.push(error);
            });
        },
        signUp: (username: String, password: String): void => {
            axios.post(client.api.authPath.toString(),
                {
                    username: username,
                    password: password
                }
            ).then(result => {
                client.auth.username = username;
                client.auth.password = password;
                client.auth.token = result.data.token;
            }).catch(error => {
                client.errors.push(error);
            });
        }
    },
    tags: {
        cache: [],
        getAll: (page: Number, size: Number): void => {
            client.tags.cache = [];
            samples.tags.forEach(a => client.tags.cache.push(a));
            axios.get(client.api.tagsPath.toString(),
                {
                    params: {
                        page: page,
                        size: size
                    }
                }
            ).then(response => {
                client.tags.cache = [];
                response.data.tags.forEach(a => client.tags.cache.push(a));
            }).catch(error => {
                client.errors.push(error);
            });
        },
        getById: (id: Number) => {
            return client.tags.cache.filter(t => t.id === id)[0];
        }
    },
    articles: {
        cache: [],
        myCache: [],
        getAll: (page: Number, size: Number, text: String, tags: TagDto[]): void => {
            client.articles.cache = [];
            samples.articles.forEach(a => client.articles.cache.push(a));
            axios.get(client.api.articlesPath.toString(),
                {
                    params: {
                        page: page,
                        size: size,
                        search: text,
                        tags: tags.map(tag => tag.id)
                    }
                }
            ).then(response => {
                client.articles.cache = [];
                response.data.articles.forEach(a => client.articles.cache.push(a));
            }).catch(error => {
                client.errors.push(error);
            });
        },
        getAllMy: (page: Number, size: Number, text: String, tags: TagDto[]): void => {
            client.articles.myCache = [];
            samples.articles.forEach(a => client.articles.myCache.push(a));
            axios.get(client.api.articlesAuthorPath.toString(),
                {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    },
                    params: {
                        page: page,
                        size: size,
                        search: text,
                        tags: tags.map(t => t.id)
                    }
                }
            ).then(response => {
                client.articles.myCache = [];
                response.data.articles.forEach(a => client.articles.myCache.push(a));
            }).catch(error => {
                client.errors.push(error);
            });
        },
        create: (article: ArticleUpdateDto): void => {
            axios.post(client.api.articlesPath.toString(),
                article, {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    }
                }
            ).then(response => {
                client.articles.getAllMy(1, 100, '', []);
            }).catch(error => {
                client.errors.push(error);
            });
        },
        update: (id: Number, article: ArticleUpdateDto): void => {
            axios.put(client.api.apiPath + `/articles/${id}`,
                article, {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    }
                }
            ).then(response => {
                client.articles.getAllMy(1, 100, '', []);
            }).catch(error => {
                client.errors.push(error);
            });
        },
        deleteById: (id: Number): void => {
            axios.delete(client.api.apiPath + `/articles/${id}`,
                {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    }
                }
            ).then(response => {
                client.articles.getAllMy(1, 100, '', []);
            }).catch(error => {
                client.errors.push(error);
            });
        }
    },
    problems: {
        cache: [],
        myCache: [],
        getAll: (page: Number, size: Number, text: String, tags: TagDto[]): void => {
            client.problems.cache = [];
            samples.problems.forEach(a => client.problems.cache.push(a));
            axios.get(client.api.problemsPath.toString(),
                {
                    params: {
                        page: page,
                        size: size,
                        search: text,
                        tags: tags.map(t => t.id)
                    }
                }
            ).then(response => {
                client.problems.cache = [];
                response.data.problems.forEach(a => client.problems.cache.push(a));
            }).catch(error => {
                client.errors.push(error);
            });
        },
        getAllMy: (page: Number, size: Number, text: String, tags: TagDto[]): void => {
            client.problems.myCache = [];
            samples.problems.forEach(a => client.problems.myCache.push(a));
            axios.get(client.api.problemsAuthorPath.toString(),
                {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    },
                    params: {
                        page: page,
                        size: size,
                        search: text,
                        tags: tags.map(t => t.id)
                    }
                }
            ).then(response => {
                client.problems.myCache = [];
                response.data.problems.forEach(a => client.problems.myCache.push(a));
            }).catch(error => {
                client.errors.push(error);
            });
        },
        getById: (id: Number): ProblemDto => {
            return client.problems.cache.filter(p => p.id === id)[0];
        },
        create: (problem: ProblemUpdateDto): void => {
            axios.post(client.api.problemsPath.toString(),
                problem, {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    }
                }
            ).then(response => {
                client.problems.getAllMy(1, 100, '', []);
            }).catch(error => {
                client.errors.push(error);
            });
        },
        update: (id: Number, problem: ProblemUpdateDto): void => {
            axios.put(client.api.apiPath + `/problems/${id}`,
                problem, {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    }
                }
            ).then(response => {
                client.problems.getAllMy(1, 100, '', []);
            }).catch(error => {
                client.errors.push(error);
            });
        },
        deleteById: (id: Number): void => {
            axios.delete(client.api.apiPath + `/problems/${id}`,
                {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    }
                }
            ).then(response => {
                client.problems.getAllMy(1, 100, '', []);
            }).catch(error => {
                client.errors.push(error);
            });
        }
    },
    submissions: {
        cache: [],
        myCache: [],
        getAll: (page: Number, size: Number, problem: ProblemDto): void => {
            client.submissions.cache = [];
            samples.submissions.forEach(a => client.submissions.cache.push(a));
            axios.get(client.api.apiPath + `/problems/${problem.id}/submissions`,
                {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    },
                    params: {
                        page: page,
                        size: size
                    }
                }
            ).then(response => {
                client.submissions.cache = [];
                response.data.submissions.forEach(a => client.submissions.cache.push(a));
            }).catch(error => {
                client.errors.push(error);
            });
        },
        getAllMy: (page: Number, size: Number): void => {
            client.submissions.myCache = [];
            samples.submissions.forEach(a => client.submissions.myCache.push(a));
            axios.get(client.api.submissionsAuthorPath.toString(),
                {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    },
                    params: {
                        page: page,
                        size: size
                    }
                }
            ).then(response => {
                client.submissions.myCache = [];
                response.data.submissions.forEach(a => client.submissions.myCache.push(a));
            }).catch(error => {
                client.errors.push(error);
            });
        },
        create: (problem: ProblemDto, submission: SubmissionCreateDto): void => {
            axios.post(client.api.apiPath + `/problems/${problem.id}/submissions`,
                submission, {
                    headers: {
                        'Authorization': `Bearer ${client.auth.token}`
                    }
                }
            ).then(response => {
                client.submissions.getAllMy(1, 100);
                client.submissions.getAll(1, 100, problem);
            }).catch(error => {
                client.errors.push(error);
            });
        }
    },
    images: {
        get: async (name: String): Promise<Blob> => {
            let data: Blob;
            axios.get(client.api.basePath.toString() + `/images/${name}`, {
                headers: {
                    'Authorization': `Bearer ${client.auth.token}`
                },
                responseType: 'blob'
            }).then(response => {
                data = response.data;
            }).catch(error => {
                client.errors.push(error);
            });
            return data;
        },
        save: (data: Blob): String => {
            const formData = new FormData();
            formData.append('image', data);

            let imageName: String;
            axios.post(client.api.apiPath + '/images', formData, {
                headers: {
                    'Authorization': `Bearer ${client.auth.token}`,
                    'Content-Type': 'multipart/form-data'
                }
            }).then(response => {
                imageName = response.data.imageName;
            }).catch(error => {
                client.errors.push(error);
            });
            return client.api.basePath + '/images/' + imageName;
        }
    }
});