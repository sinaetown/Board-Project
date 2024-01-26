package com.encore.board.post.service;

import com.encore.board.post.domain.Post;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostScheduler {
    private final PostRepository postRepository;

    @Autowired
    public PostScheduler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

//    초 분 시간 일 월 요일 형태로 스케쥴링 설정
//    * : 매 초(분/시 등)을 의미
//    특정 숫자 : 특정숫자의 초(분/시 등)를 의미
//    0/특정숫자 : 특정숫자마다
//    ex1) 0 0 * * * * => 매일 0분 0초에 스케쥴링 시작
//    ex2) 0 0/1 * * * * => 매일 1분마다 0초에 스케쥴링 시작
//    ex3) 0/1 * * * * * => 매초마다
//    ex4) 0 0 11 * * * => 매일 11시에 스케쥴링
    @Scheduled(cron="0 0/1 * * * *")
    public void postSchedule() {
//        System.out.println("===Scheduler START!===");
        Page<Post> posts = postRepository.findByAppointment("yes", Pageable.unpaged());
        for(Post p : posts.getContent()){
            if(p.getAppointmentTime().isBefore(LocalDateTime.now())){
                p.updateAppointment();
                postRepository.save(p);
            }
        }
//        System.out.println("===Scheduler ENDED!===");

    }

}
