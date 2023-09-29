package crm_project_22.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import crm_project_22.entity.User;

@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();
        String context = req.getContextPath();

        HttpSession httpSession = req.getSession();
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");

        switch (path) {
        case "/login":
            if (userLoggedIn == null) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(context);
            }
            break;

        default:
            String regex = "/(bootstrap|css|js|less|plugins)/.*";

            if (userLoggedIn == null && !path.matches(regex)) {
                res.sendRedirect(context + "/login");
            } else {
                chain.doFilter(request, response);
            }
            break;
        }
    }
}
