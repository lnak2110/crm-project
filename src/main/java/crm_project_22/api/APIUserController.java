package crm_project_22.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import crm_project_22.entity.User;
import crm_project_22.payload.response.BaseResponse;
import crm_project_22.service.UserService;

@WebServlet(name = "apiUserController", urlPatterns = { "/api/users", "/api/users/outside",
        "/api/users/in-project", "/api/users/detail", "/api/users/profile" })
public class APIUserController extends HttpServlet {
    private UserService userService = new UserService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        BaseResponse baseResponse;

        if (path.startsWith("/api/users/outside")) {
            int pid = Integer.parseInt(req.getParameter("pid"));
            List<User> users = userService.getManyOutsideProject(pid);
            baseResponse = new BaseResponse(200, users != null ? "Get successful" : "Get failed",
                    users);
        } else if (path.startsWith("/api/users/in-project")) {
            int pid = Integer.parseInt(req.getParameter("pid"));
            List<User> users = userService.getManyInAProject(pid);
            baseResponse = new BaseResponse(200, users != null ? "Get successful" : "Get failed",
                    users);
        } else if (path.startsWith("/api/users/profile")) {
            int id = Integer.parseInt(req.getParameter("id"));
            String pwParam = req.getParameter("pw");
            User user;

            if (pwParam != null && Integer.parseInt(pwParam) == 1) {
                user = userService.getById(id, true);
            } else {
                user = userService.getById(id, false);
            }
            baseResponse = new BaseResponse(200, user != null ? "Get successful" : "Get failed",
                    user);
        } else if (path.startsWith("/api/users/detail")) {
            int id = Integer.parseInt(req.getParameter("id"));
            int pid = Integer.parseInt(req.getParameter("pid"));
            User user = userService.getOneInAProject(id, pid);
            baseResponse = new BaseResponse(200, user != null ? "Get successful" : "Get failed",
                    user);
        } else if (path.startsWith("/api/users")) {
            List<User> users = userService.getAll();
            baseResponse = new BaseResponse(200, users != null ? "Get successful" : "Get failed",
                    users);
        } else {
            return;
        }

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.print(dataJSON);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!"/api/users".equals(req.getServletPath())) {
            return;
        }

        boolean result = userService.create(req, resp);

        BaseResponse baseResponse = new BaseResponse(200,
                result ? "Create successful" : "Create failed", result);

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.print(dataJSON);
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!"/api/users".equals(req.getServletPath())) {
            return;
        }

        boolean result = userService.updateById(req, resp);

        BaseResponse baseResponse = new BaseResponse(200,
                result ? "Update successful" : "Update failed", result);

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.print(dataJSON);
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        int id = Integer.parseInt(req.getParameter("id"));
        boolean result;

        if (path.startsWith("/api/users/in-project")) {
            int pid = Integer.parseInt(req.getParameter("pid"));

            result = userService.deleteFromProject(id, pid);
        } else if (path.startsWith("/api/users")) {
            result = userService.deleteById(id);
        } else {
            return;
        }

        BaseResponse baseResponse = new BaseResponse(200,
                result ? "Delete successful" : "Delete failed", result);

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.println(dataJSON);
        out.flush();
    }
}
