package com.gurnoor.music_event.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gurnoor.music_event.model.Performance;

import java.time.LocalDate;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Value("${festival.start-date}")
    private String festivalStartDate;

    @Value("${festival.end-date}")
    private String festivalEndDate;

    // Logs after a performance is successfully created
    @AfterReturning(
        pointcut = "execution(* com.gurnoor.music_event.service.PerformanceService.createPerformance(..))",
        returning = "result"
    )
    public void logAfterCreate(JoinPoint jp, Object result) {
        if (result instanceof Performance p) {
            log.info("✅ Performance CREATED — Artist: {}, Stage: {}, Date: {}, Time: {} - {}",
                    p.getArtist().getName(),
                    p.getStage().getName(),
                    p.getDate(),
                    p.getStartTime(),
                    p.getEndTime());
        }
    }

    // Logs after a performance is successfully updated
    @AfterReturning(
        pointcut = "execution(* com.gurnoor.music_event.service.PerformanceService.updatePerformance(..))",
        returning = "result"
    )
    public void logAfterUpdate(JoinPoint jp, Object result) {
        if (result instanceof Performance p) {
            log.info("Performance UPDATED — Artist: {}, Stage: {}, Date: {}, Time: {} - {}",
                    p.getArtist().getName(),
                    p.getStage().getName(),
                    p.getDate(),
                    p.getStartTime(),
                    p.getEndTime());
        }
    }

    // Logs after a performance is cancelled/deleted
    @AfterReturning(
        pointcut = "execution(* com.gurnoor.music_event.service.PerformanceService.deletePerformance(..))"
    )
    public void logAfterDelete(JoinPoint jp) {
        Object[] args = jp.getArgs();
        log.info("❌ Performance CANCELLED — ID: {}", args[0]);
    }

    // Before advice — warns if date is outside festival dates
    @Before("execution(* com.gurnoor.music_event.service.PerformanceService.createPerformance(..))")
    public void checkFestivalDates(JoinPoint jp) {
        Object[] args = jp.getArgs();
        // 3rd argument is the Performance object
        if (args.length >= 3 && args[2] instanceof Performance p) {
            LocalDate start = LocalDate.parse(festivalStartDate);
            LocalDate end   = LocalDate.parse(festivalEndDate);
            LocalDate date  = p.getDate();
            if (date != null && (date.isBefore(start) || date.isAfter(end))) {
                log.warn("WARNING — Performance scheduled on {} is OUTSIDE festival dates ({} to {})",
                        date, festivalStartDate, festivalEndDate);
            }
        }
    }
}