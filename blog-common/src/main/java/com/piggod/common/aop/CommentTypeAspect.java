package com.piggod.common.aop;

import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.query.CommentPageQuery;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.piggod.common.constants.SystemConstants.ARTICLE_COMMENT;
import static com.piggod.common.constants.SystemConstants.LINK_COMMENT;

@Aspect
@Component
public class CommentTypeAspect {

    // 定义切点：拦截特定接口
    @Pointcut("execution(* com.piggod.blogfront.controller.CommentController.getCommentList(..))")
    public void commentListPointcut() {}

    @Pointcut("execution(* com.piggod.blogfront.controller.CommentController.getLinkCommentList(..))")
    public void linkCommentListPointcut() {}

    // 环绕通知：设置 type 参数
    @Around("commentListPointcut() || linkCommentListPointcut()")
    public Object setCommentType(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof CommentPageQuery) {
                CommentPageQuery query = (CommentPageQuery) arg;
                // 根据切点路径设置 type
                if (joinPoint.getSignature().getName().contains("getLinkCommentList")) {
                    query.setArticleId(null);
                    query.setType(LINK_COMMENT); // 友链评论
                } else {
                    query.setType(ARTICLE_COMMENT); // 文章评论
                }
                break;
            }
        }
        return joinPoint.proceed(args);
    }
}