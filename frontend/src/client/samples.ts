import {ArticleDto, ProblemDto, SubmissionDto, TagDto} from "./data";
import {reactive} from "vue";


interface SamplesInterface {
    articles: ArticleDto[],
    problems: ProblemDto[],
    submissions: SubmissionDto[],
    tags: TagDto[]
}

export const samples: SamplesInterface = reactive({
    tags: [
        {id: 1, tag: 'math'},
        {id: 2, tag: 'graph'},
        {id: 3, tag: 'dynamic-programming'},
        {id: 4, tag: 'tree'},
        {id: 5, tag: 'geometry'},
        {id: 6, tag: 'strings'},
    ],
    articles: [
        {
            id: 1,
            authorId: 0,
            authorUsername: 'Ray_1024',
            creationDate: new Date(),
            title: 'Sample Article #1',
            article: '# 1 \n## 1 \n11111111111111111111111111111111111111111111111',
            tags: [1, 2]
        },
        {
            id: 2,
            authorId: 0,
            authorUsername: 'Ray_1024',
            creationDate: new Date(),
            title: 'Sample Article #2',
            article: '222222222222222222222222222222222222222222',
            tags: [3, 4]
        }
    ],
    problems: [
        {
            id: 1,
            authorId: 0,
            authorUsername: 'Ray_1024',
            creationDate: new Date(),
            title: 'Sum Two',
            description: 'Sum A and B',
            input: 'A is integer number, B is integer number',
            output: 'C = A + B',
            memoryLimitBytes: 100000000,
            timeLimitMilliseconds: 1000,
            samples: [
                {
                    id: 0,
                    input: '2 3',
                    output: '5'
                },
                {
                    id: 1,
                    input: '10 99',
                    output: '109'
                },
            ],
            tests: [
                {
                    id: 2,
                    input: '2 3',
                    output: '5'
                },
                {
                    id: 3,
                    input: '10 99',
                    output: '109'
                },
            ],
            tags: [3, 1]
        }, {
            id: 2,
            authorId: 0,
            authorUsername: 'Ray_1024',
            creationDate: new Date(),
            title: 'Sum Three',
            description: 'Sum A, B and C',
            input: 'A is integer number, B is integer number, C is integer number',
            output: 'D = A + B + C',
            memoryLimitBytes: 100000000,
            timeLimitMilliseconds: 1000,
            samples: [
                {
                    id: 4,
                    input: '2 3 5',
                    output: '10'
                },
                {
                    id: 5,
                    input: '10 99 1',
                    output: '110'
                },
            ],
            tests: [
                {
                    id: 6,
                    input: '2 3 5',
                    output: '10'
                },
                {
                    id: 7,
                    input: '10 99 1',
                    output: '110'
                },
            ],
            tags: [3, 1]
        }
    ],
    submissions: [
        {
            problemId: 1,
            status: 'ACCEPTED',
            creationTime: new Date(),
            language: 'C++',
            sourceCode: '#include <iostream>\n' +
                'int main() {\n' +
                '    int a, b;\n' +
                '  \tstd::cin >> a >> b;\n' +
                '    std::cout << a + b;\n' +
                '    return 0;\n' +
                '}'
        },
        {
            problemId: 2,
            status: 'MEMORY_LIMIT_EXCEEDED',
            creationTime: new Date(),
            language: 'Java',
            sourceCode: 'import java.util.*;\n' +
                '\n' +
                'class GFG {\n' +
                '    static void swapValuesUsingThirdVariable(int m, int n)\n' +
                '    {\n' +
                '        int temp = m;\n' +
                '        m = n;\n' +
                '        n = temp;\n' +
                '        System.out.println("Value of m is " + m\n' +
                '                           + " and Value of n is " + n);\n' +
                '    }\n' +
                '\n' +
                '    public static void main(String[] args)\n' +
                '    {\n' +
                '        int m = 9, n = 5;\n' +
                '        swapValuesUsingThirdVariable(m, n);\n' +
                '    }\n' +
                '}'
        },
        {
            problemId: 2,
            status: 'TIME_LIMIT_EXCEEDED',
            creationTime: new Date(),
            language: 'C++',
            sourceCode: '#include <iostream>\n' +
                'int main() {\n' +
                '    int a, b;\n' +
                '  \tstd::cin >> a >> b;\n' +
                '    std::cout << a + b;\n' +
                '    return 0;\n' +
                '}'
        },
        {
            problemId: 1,
            status: 'WRONG_ANSWER',
            creationTime: new Date(),
            language: 'Java',
            sourceCode: 'import java.util.*;\n' +
                '\n' +
                'class GFG {\n' +
                '    static void swapValuesUsingThirdVariable(int m, int n)\n' +
                '    {\n' +
                '        int temp = m;\n' +
                '        m = n;\n' +
                '        n = temp;\n' +
                '        System.out.println("Value of m is " + m\n' +
                '                           + " and Value of n is " + n);\n' +
                '    }\n' +
                '\n' +
                '    public static void main(String[] args)\n' +
                '    {\n' +
                '        int m = 9, n = 5;\n' +
                '        swapValuesUsingThirdVariable(m, n);\n' +
                '    }\n' +
                '}'
        }
    ],
});