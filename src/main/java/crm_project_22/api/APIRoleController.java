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

import crm_project_22.entity.Role;
import crm_project_22.payload.response.BaseResponse;
import crm_project_22.service.RoleService;

@WebServlet(name = "apiRoleController", urlPatterns = "/api/roles")
public class APIRoleController extends HttpServlet {
    private RoleService roleService = new RoleService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Role> roles = roleService.getAllRoles();

        BaseResponse baseResponse = new BaseResponse(200,
                roles != null ? "Get successful" : "Get failed", roles);

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.print(dataJSON);
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        boolean result = roleService.updateById(req, resp);

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
        boolean result = roleService.deleteById(id);
        
        BaseResponse baseResponse = new BaseResponse(200,
                result ? "Delete successful" : "Delete failed", result);

        resp.setContentType("application/json; charset=UTF-8");

        String dataJSON = gson.toJson(baseResponse);

        PrintWriter out = resp.getWriter();
        out.println(dataJSON);
        out.flush();
    }
}
