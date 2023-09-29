package crm_project_22.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import crm_project_22.entity.Task;
import crm_project_22.payload.response.BaseResponse;
import crm_project_22.service.TaskService;

@WebServlet(name = "apiTaskController", urlPatterns = { "/api/tasks", "/api/tasks/detail",
        "/api/tasks/no-user", "/api/tasks/user", "/api/tasks/update-status" })
public class APITaskController extends HttpServlet {
    private TaskService taskService = new TaskService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        BaseResponse baseResponse;

        if (path.startsWith("/api/tasks/detail")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Task task = taskService.getById(id);
            baseResponse = new BaseResponse(200, task != null ? "Get successful" : "Get failed",
                    task);
        } else if (path.startsWith("/api/tasks/no-user")) {
            int pid = Integer.parseInt(req.getParameter("pid"));
            List<Task> tasks = taskService.getWithNoUserInProject(pid);
            baseResponse = new BaseResponse(200, tasks != null ? "Get successful" : "Get failed",
                    tasks);
        } else if (path.startsWith("/api/tasks/user")) {
            int uid = Integer.parseInt(req.getParameter("uid"));
            List<Task> tasks = taskService.getAllOfOneUser(uid);
            baseResponse = new BaseResponse(200, tasks != null ? "Get successful" : "Get failed",
                    tasks);
        } else if (path.startsWith("/api/tasks")) {
            List<Task> tasks = taskService.getAll();
            baseResponse = new BaseResponse(200, tasks != null ? "Get successful" : "Get failed",
                    tasks);
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
        boolean result = taskService.create(req, resp);

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
        boolean result;
        String path = req.getServletPath();

        switch (path) {
        case "/api/tasks":
            result = taskService.updateById(req, resp);
            break;

        case "/api/tasks/update-status":
            int id = Integer.parseInt(req.getParameter("id"));
            int sid = Integer.parseInt(req.getParameter("sid"));

            result = taskService.updateStatus(id, sid);
            break;

        default:
            return;
        }

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
        int id = Integer.parseInt(req.getParameter("id"));
        boolean result = taskService.deleteById(id);

        BaseResponse baseResponse = new BaseResponse(200,
                result ? "Delete successful" : "Delete failed", result);

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.println(dataJSON);
        out.flush();
    }
}