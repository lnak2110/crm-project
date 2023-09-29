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

import crm_project_22.entity.Status;
import crm_project_22.payload.response.BaseResponse;
import crm_project_22.service.StatusService;

@WebServlet(name = "apiStatusController", urlPatterns = { "/api/statuses",
        "/api/statuses/percents/in-project", "/api/statuses/percents/user-in-project",
        "/api/statuses/percents/user", "/api/statuses/percents/leader" })
public class APIStatusController extends HttpServlet {
    private StatusService statusService = new StatusService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        BaseResponse baseResponse;

        List<Status> result = null;

        if (path.startsWith("/api/statuses/percents/in-project")) {
            int pid = Integer.parseInt(req.getParameter("pid"));

            result = statusService.getPercentsInProject(pid);
        } else if (path.startsWith("/api/statuses/percents/user-in-project")) {
            int uid = Integer.parseInt(req.getParameter("uid"));
            int pid = Integer.parseInt(req.getParameter("pid"));

            result = statusService.getPercentsOfOneUserInProject(uid, pid);
        } else if (path.startsWith("/api/statuses/percents/user")) {
            int uid = Integer.parseInt(req.getParameter("uid"));

            result = statusService.getPercentsOfOneUser(uid);
        } else if (path.startsWith("/api/statuses/percents/leader")) {
            int uid = Integer.parseInt(req.getParameter("uid"));

            result = statusService.getPercentsOfOneLeader(uid);
        } else if (path.startsWith("/api/statuses")) {
            result = statusService.getAll();
        } else {
            return;
        }

        baseResponse = new BaseResponse(200, result != null ? "Get successful" : "Get failed",
                result);

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.print(dataJSON);
        out.flush();
    }
}
