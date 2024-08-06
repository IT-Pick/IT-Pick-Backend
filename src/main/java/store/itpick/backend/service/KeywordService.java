package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.model.Keyword;
import store.itpick.backend.repository.KeywordRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public void saveAll(List<Keyword> keywords) {
        for (Keyword keyword : keywords) {
            Optional<Keyword> existingKeyword = keywordRepository.findFirstByRedisIdAndKeyword(keyword.getRedisId(), keyword.getKeyword());

            if (existingKeyword.isPresent()) {
                // 기존의 엔티티가 존재하면, 해당 엔티티의 필드 값을 업데이트
                Keyword keywordToUpdate = existingKeyword.get();
                keywordToUpdate.setStatus(keyword.getStatus());
                keywordToUpdate.setReference(keyword.getReference());
                keywordToUpdate.setUpdateAt(Timestamp.from(Instant.now())); // 업데이트 시간 변경
                // 필요한 경우 다른 필드도 업데이트

                keywordRepository.save(keywordToUpdate);
            } else {
                // 존재하지 않으면 새로운 엔티티를 저장
                keywordRepository.save(keyword);
            }
        }
    }

    @Transactional
    public void performDailyTasksNate() {
        log.info("Starting scheduled tasks...performing DailyTask");

        Pageable pageable = PageRequest.of(0, 10);  // 페이지 0, 크기 10


        // 현재 날짜를 yyyy_MM_dd 형식으로 포맷
        String currentDate = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yy_MM_dd"));

        // redis_id가 'nate_by_real_time'인 레코드 중에서 최신 10개를 조회
        List<Keyword> recentKeywords = keywordRepository.findTop10ByRedisIdForNate(pageable);
        int i=1;
        // 기존 레코드의 redis_id를 현재 날짜로 설정하여 새로운 레코드를 생성
        for (Keyword keyword : recentKeywords) {
            System.out.println(i++);
            Keyword newKeyword = new Keyword();
            newKeyword.setKeyword(keyword.getKeyword()); // 기존 키워드를 복사
            newKeyword.setRedisId("nate_" + currentDate); // 새로운 redis_id 설정
            newKeyword.setStatus("active"); // 기본값 설정
            newKeyword.setCreateAt(Timestamp.from(Instant.now())); // 현재 시각으로 생성
            newKeyword.setUpdateAt(Timestamp.from(Instant.now())); // 현재 시각으로 업데이트

            // 기존의 reference를 그대로 유지
            newKeyword.setReference(keyword.getReference());

            // 새로운 키워드 저장
            keywordRepository.save(newKeyword);
        }

        log.info("Scheduled tasks completed DailyTask.");
    }
    @Transactional
    public void performDailyTasksNaver() {
        log.info("Starting scheduled tasks...performing DailyTask");

        Pageable pageable = PageRequest.of(0, 10);  // 페이지 0, 크기 10


        // 현재 날짜를 yyyy_MM_dd 형식으로 포맷
        String currentDate = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yy_MM_dd"));

        // redis_id가 'nate_by_real_time'인 레코드 중에서 최신 10개를 조회
        List<Keyword> recentKeywords = keywordRepository.findTop10ByRedisIdForNaver(pageable);
        int i=1;
        // 기존 레코드의 redis_id를 현재 날짜로 설정하여 새로운 레코드를 생성
        for (Keyword keyword : recentKeywords) {
            System.out.println(i++);
            Keyword newKeyword = new Keyword();
            newKeyword.setKeyword(keyword.getKeyword()); // 기존 키워드를 복사
            newKeyword.setRedisId("naver_" + currentDate); // 새로운 redis_id 설정
            newKeyword.setStatus("active"); // 기본값 설정
            newKeyword.setCreateAt(Timestamp.from(Instant.now())); // 현재 시각으로 생성
            newKeyword.setUpdateAt(Timestamp.from(Instant.now())); // 현재 시각으로 업데이트

            // 기존의 reference를 그대로 유지
            newKeyword.setReference(keyword.getReference());

            // 새로운 키워드 저장
            keywordRepository.save(newKeyword);
        }

        log.info("Scheduled tasks completed DailyTask.");
    }
    @Transactional
    public void performDailyTasksZum() {
        log.info("Starting scheduled tasks...performing DailyTask");

        Pageable pageable = PageRequest.of(0, 10);  // 페이지 0, 크기 10


        // 현재 날짜를 yyyy_MM_dd 형식으로 포맷
        String currentDate = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yy_MM_dd"));

        // redis_id가 'nate_by_real_time'인 레코드 중에서 최신 10개를 조회
        List<Keyword> recentKeywords = keywordRepository.findTop10ByRedisIdForZum(pageable);
        int i=1;
        // 기존 레코드의 redis_id를 현재 날짜로 설정하여 새로운 레코드를 생성
        for (Keyword keyword : recentKeywords) {
            System.out.println(i++);
            Keyword newKeyword = new Keyword();
            newKeyword.setKeyword(keyword.getKeyword()); // 기존 키워드를 복사
            newKeyword.setRedisId("zum_" + currentDate); // 새로운 redis_id 설정
            newKeyword.setStatus("active"); // 기본값 설정
            newKeyword.setCreateAt(Timestamp.from(Instant.now())); // 현재 시각으로 생성
            newKeyword.setUpdateAt(Timestamp.from(Instant.now())); // 현재 시각으로 업데이트

            // 기존의 reference를 그대로 유지
            newKeyword.setReference(keyword.getReference());

            // 새로운 키워드 저장
            keywordRepository.save(newKeyword);
        }

        log.info("Scheduled tasks completed DailyTask.");
    }
}
