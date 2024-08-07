package store.itpick.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.itpick.backend.model.CommunityPeriod;
import store.itpick.backend.model.Keyword;
import store.itpick.backend.repository.CommunityPeriodRepository;
import store.itpick.backend.repository.KeywordRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private CommunityPeriodRepository communityPeriodRepository;



    public List<Keyword> saveAll(List<Keyword> keywords) {
        return keywordRepository.saveAll(keywords);
    }
    public void save(Keyword keyword) {
        keywordRepository.save(keyword);
    }

    // 모든 키워드를 Set으로 반환하는 메서드
    public Set<String> findAllKeywordsAsSet() {
        List<Keyword> keywords = keywordRepository.findAll(); // 모든 키워드 조회
        return keywords.stream()
                .map(Keyword::getKeyword) // Keyword 객체에서 키워드 문자열 추출
                .collect(Collectors.toSet()); // Set으로 변환
    }

    public Optional<Keyword> findByKeyword(String keyword) {
        return keywordRepository.findByKeyword(keyword);
    }

    @Transactional
    public void performDailyTasksNate() {
        log.info("Starting scheduled tasks...performing DailyTask");

        Pageable pageable = PageRequest.of(0, 10);  // 페이지 0, 크기 10

        // period가 'realtime'이고 community가 'naver'인 레코드 중에서 최신 10개를 조회
        List<CommunityPeriod> recentCommunityPeriods = communityPeriodRepository.findTop10ByNateAndRealtime(pageable);

        // 새로운 period 값
        // 현재 날짜를 yyMMdd 형식으로 포맷
        String currentDate = Instant.now().atZone(ZoneId.systemDefault())


                .format(DateTimeFormatter.ofPattern("yyMMdd"));
        for (CommunityPeriod communityPeriod : recentCommunityPeriods) {
            CommunityPeriod newCommunityPeriod = new CommunityPeriod();
            newCommunityPeriod.setCommunity(communityPeriod.getCommunity()); // 기존 community 값 설정
            newCommunityPeriod.setPeriod(currentDate); // 새로운 period 값 설정

            // 새로운 CommunityPeriod 저장
            communityPeriodRepository.save(newCommunityPeriod);
            log.info("Saved new CommunityPeriod with period: {}", currentDate);
        }

        log.info("Scheduled tasks completed DailyTask.");
    }
    @Transactional
    public void performDailyTasksNaver() {
        log.info("Starting scheduled tasks...performing DailyTask");

        Pageable pageable = PageRequest.of(0, 10);  // 페이지 0, 크기 10

        // period가 'realtime'이고 community가 'naver'인 레코드 중에서 최신 10개를 조회
        List<CommunityPeriod> recentCommunityPeriods = communityPeriodRepository.findTop10ByNaverAndRealtime(pageable);

        // 새로운 period 값
        // 현재 날짜를 yyMMdd 형식으로 포맷
        String currentDate = Instant.now().atZone(ZoneId.systemDefault())


                .format(DateTimeFormatter.ofPattern("yyMMdd"));
        for (CommunityPeriod communityPeriod : recentCommunityPeriods) {
            CommunityPeriod newCommunityPeriod = new CommunityPeriod();
            newCommunityPeriod.setCommunity(communityPeriod.getCommunity()); // 기존 community 값 설정
            newCommunityPeriod.setPeriod(currentDate); // 새로운 period 값 설정

            // 새로운 CommunityPeriod 저장
            communityPeriodRepository.save(newCommunityPeriod);
            log.info("Saved new CommunityPeriod with period: {}", currentDate);
        }

        log.info("Scheduled tasks completed DailyTask.");
    }
    @Transactional
    public void performDailyTasksZum() {
        log.info("Starting scheduled tasks...performing DailyTask");

        Pageable pageable = PageRequest.of(0, 10);  // 페이지 0, 크기 10

        // period가 'realtime'이고 community가 'naver'인 레코드 중에서 최신 10개를 조회
        List<CommunityPeriod> recentCommunityPeriods = communityPeriodRepository.findTop10ByZumAndRealtime(pageable);

        // 새로운 period 값
        // 현재 날짜를 yyMMdd 형식으로 포맷
        String currentDate = Instant.now().atZone(ZoneId.systemDefault())


                .format(DateTimeFormatter.ofPattern("yyMMdd"));
        for (CommunityPeriod communityPeriod : recentCommunityPeriods) {
            CommunityPeriod newCommunityPeriod = new CommunityPeriod();
            newCommunityPeriod.setCommunity(communityPeriod.getCommunity()); // 기존 community 값 설정
            newCommunityPeriod.setPeriod(currentDate); // 새로운 period 값 설정

            // 새로운 CommunityPeriod 저장
            communityPeriodRepository.save(newCommunityPeriod);
            log.info("Saved new CommunityPeriod with period: {}", currentDate);
        }

        log.info("Scheduled tasks completed DailyTask.");
    }
}
