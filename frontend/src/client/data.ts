export interface TagDto {
    id: Number,
    tag: String
}

export interface ArticleDto {
    id: Number,
    authorId: Number,
    authorUsername: String,
    creationDate: Date,
    title: String,
    article: String,
    tags: Number[];
}

export interface TestCaseDto {
    id: Number,
    input: String,
    output: String
}

export interface ProblemDto {
    id: Number,
    authorId: Number,
    authorUsername: String,
    creationDate: Date,
    title: String,
    description: String,
    input: String,
    output: String,
    memoryLimitBytes: Number,
    timeLimitMilliseconds: Number,
    samples: TestCaseDto[],
    tests: TestCaseDto[],
    tags: Number[]
}

export interface SubmissionDto {
    problemId: Number
    status: String,
    creationTime: Date,
    language: String,
    sourceCode: String
}

export interface ErrorDto {
    message: String,
    timestamp: Date
}

export interface ArticleUpdateDto {
    title: String,
    article: String,
    tags: Number[]
}

export interface ProblemUpdateDto {
    title: String,
    description: String,
    input: String,
    output: String,
    memoryLimitBytes: Number,
    timeLimitMilliseconds: Number,
    samples: TestCaseDto[],
    tests: TestCaseDto[],
    tags: Number[]
}

export interface SubmissionCreateDto {
    language: String,
    sourceCode: String
}

export interface TagsDto {
    tags: TagDto[]
}

export interface PageDto {
    page: Number
}

export interface SearchParams {
    text: String,
    tags: TagDto[]
}