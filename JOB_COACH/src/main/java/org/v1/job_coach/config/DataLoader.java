package org.v1.job_coach.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.v1.job_coach.entity.chat.Question;
import org.v1.job_coach.repository.chat.QuestionRepository;

//@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(QuestionRepository questionRepository) {
        return args -> {
            // 20개의 질문 내용 Test
            String[] questions = {
                    "본인의 강점과 약점에 대해 설명해 주세요.",
                    "왜 이 회사에 지원하게 되었나요?",
                    "본인의 주요 성과나 프로젝트를 소개해 주세요.",
                    "팀워크를 잘 발휘했던 경험에 대해 설명해 주세요.",
                    "압박 상황에서 어떻게 대처하나요?",
                    "직무와 관련된 기술이나 도구에 대해 설명해 주세요.",
                    "5년 후 자신의 커리어 목표는 무엇인가요?",
                    "자신이 직면했던 가장 큰 문제는 무엇이었으며, 어떻게 해결했나요?",
                    "회사에서 어떤 가치를 더할 수 있을 것 같나요?",
                    "리더십 경험이 있다면 그 경험을 설명해 주세요.",
                    "스트레스가 많은 상황에서 어떻게 자신을 관리하나요?",
                    "다양한 의견을 조율했던 경험에 대해 설명해 주세요.",
                    "회사의 비전과 목표에 어떻게 기여할 수 있을까요?",
                    "팀 내 갈등 상황에서 어떤 방식으로 문제를 해결하나요?",
                    "업무 외적으로 자기 개발을 위해 무엇을 하고 있나요?",
                    "최근 읽은 책이나 기사 중 인상 깊었던 내용을 공유해 주세요.",
                    "어떤 상황에서 결정을 내리는 것이 어려웠고, 그 이유는 무엇인가요?",
                    "업무에 필요한 새로운 기술을 배우는 방법에 대해 설명해 주세요.",
                    "자신의 역량을 개선하기 위해 어떤 노력을 하고 있나요?"
            };


            for (String questionContent : questions) {
                Question question = new Question(questionContent);
                questionRepository.save(question);
            }
        };
    }
}
