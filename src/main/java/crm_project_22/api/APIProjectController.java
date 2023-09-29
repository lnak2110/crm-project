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

import crm_project_22.entity.Project;
import crm_project_22.payload.response.BaseResponse;
import crm_project_22.service.ProjectService;

@WebServlet(name = "apiProjectController", urlPatterns = { "/api/projects", "/api/projects/detail",
        "/api/projects/add-members", "/api/projects/user" })
public class APIProjectController extends HttpServlet {
    private ProjectService projectService = new ProjectService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        BaseResponse baseResponse;

        if (path.startsWith("/api/projects/detail")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Project project = projectService.getById(id);
            baseResponse = new BaseResponse(200, project != null ? "Get successful" : "Get failed",
                    project);
        } else if (path.startsWith("/api/projects/user")) {
            int uid = Integer.parseInt(req.getParameter("uid"));
            List<Project> projects = projectService.getAllOfOneLeader(uid);
            baseResponse = new BaseResponse(200, projects != null ? "Get successful" : "Get failed",
                    projects);
        } else if (path.startsWith("/api/projects")) {
            List<Project> projects = projectService.getAll();
            baseResponse = new BaseResponse(200, projects != null ? "Get successful" : "Get failed",
                    projects);
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
        boolean result = projectService.create(req, resp);
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
        String path = req.getServletPath();
        BaseResponse baseResponse;

        if (path.startsWith("/api/projects/add-members")) {
            boolean result = projectService.addMembers(req, resp);
            baseResponse = new BaseResponse(200, result ? "Update successful" : "Update failed",
                    result);
        } else if (path.startsWith("/api/projects")) {
            boolean result = projectService.updateById(req, resp);
            baseResponse = new BaseResponse(200, result ? "Update successful" : "Update failed",
                    result);
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        boolean result = projectService.deleteById(id);

        BaseResponse baseResponse = new BaseResponse(200,
                result ? "Delete successful" : "Delete failed", result);

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.println(dataJSON);
        out.flush();
    }
}
