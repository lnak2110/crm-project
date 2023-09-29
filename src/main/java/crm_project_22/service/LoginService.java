package crm_project_22.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import crm_project_22.entity.User;
import crm_project_22.repository.UserRepository;

public class LoginService {
    private UserRepository userRepository = new UserRepository();

    public User checkLogin(HttpServletRequest req, String email, String password) {
        User user = userRepository.getByEmailAndPassword(email, password);

        if (user != null) {
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("userLoggedIn", user);
        }

        return user;
    }
}
