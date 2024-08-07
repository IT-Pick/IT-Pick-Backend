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
    private final CommunityPeriodRepository communityPeriodRepository;



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

        /** Community_period 테이블에 해당 일 튜플 추가 **/
        // 새로운 period 값
        // 현재 날짜를 yyMMdd 형식으로 포맷
        String currentDate = Instant.now().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 현재 날짜와 'naver' 커뮤니티에 대해 이미 존재하는 CommunityPeriod 조회
        Optional<CommunityPeriod> existingCommunityPeriod = communityPeriodRepository.findByCommunityAndPeriod("naver", currentDate);

        CommunityPeriod communityPeriodToSave;
        if (existingCommunityPeriod.isPresent()) {
            // 이미 존재하는 경우, 해당 CommunityPeriod 업데이트
            communityPeriodToSave = existingCommunityPeriod.get();
            // 필요시 다른 필드를 업데이트
            communityPeriodToSave.setPeriod(currentDate); // 기존 값과 같지만 명시적으로 설정
            communityPeriodRepository.save(communityPeriodToSave);
            log.info("Updated existing CommunityPeriod with period: {}", currentDate);
        } else {
            // 존재하지 않는 경우, 새로운 CommunityPeriod 생성 및 저장
            communityPeriodToSave = new CommunityPeriod();
            communityPeriodToSave.setCommunity("naver");
            communityPeriodToSave.setPeriod(currentDate);
            communityPeriodRepository.save(communityPeriodToSave);
            log.info("Saved new CommunityPeriod with period: {}", currentDate);
        }

        /**
         * 키워드 중에 가장 updateAt이 최근에 것인 것을 10개 찾고,
         * 그 10개의 keywordId를 찾아서, keyword_community_period 테이블에 keywordId와
         * 위에서 만든 community_period_id를 저장해주는 로직
         **/

        // 가장 최근에 업데이트된 10개의 키워드 조회
        List<Keyword> recentKeywords = keywordRepository.findTop10ByOrderByUpdateAtDesc(PageRequest.of(0, 10));

        // 각 키워드와 새 CommunityPeriod를 연관시키기
        for (Keyword keyword : recentKeywords) {
            keyword.getCommunityPeriods().add(communityPeriodToSave);
            keywordRepository.save(keyword); // 연관관계 업데이트 후 저장
        }
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
