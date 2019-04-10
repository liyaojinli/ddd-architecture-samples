package study.huhao.demo.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.huhao.demo.domain.models.blog.Blog;
import study.huhao.demo.domain.models.blog.BlogRepository;
import study.huhao.demo.domain.models.user.UserId;
import study.huhao.demo.domain.services.BlogDomainService;

@Service
@Transactional
public class BlogService {

    private final BlogDomainService blogDomainService;

    @Autowired
    public BlogService(BlogRepository blogRepository) {
        blogDomainService = new BlogDomainService(blogRepository);
    }

    public Blog createBlog(String title, String body, UserId author) {
        return blogDomainService.createBlog(title, body, author);
    }
}