package crm_project_22.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import crm_project_22.entity.Project;
import crm_project_22.entity.Task;
import crm_project_22.entity.TaskUser;
import crm_project_22.entity.User;
import crm_project_22.payload.response.BaseResponse;
import crm_project_22.service.ProjectService;
import crm_project_22.service.TaskService;
import crm_project_22.service.TaskUserService;

@WebFilter(filterName = "apiTaskFilter", urlPatterns = { "/api/tasks", "/api/tasks/detail",
        "/api/tasks/no-user", "/api/tasks/user", "/api/tasks/update-status" })
public class APITaskFilter implements Filter {
    private TaskService taskService = new TaskService();
    private ProjectService projectService = new ProjectService();
    private TaskUserService taskUserService = new TaskUserService();
    private Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpSession httpSession = req.getSession();
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");

        boolean isAdmin = userLoggedIn != null
                && "ADMIN".equalsIgnoreCase(userLoggedIn.getRole().getName());

        String path = req.getServletPath();
        String httpMethod = req.getMethod();

        String idParam = req.getParameter("id");

        switch (path) {
        case "/api/tasks":
            if ("GET".equalsIgnoreCase(httpMethod)) {
                chain.doFilter(request, response);
            } else if ("POST".equalsIgnoreCase(httpMethod)) {
                String idProjectParam = req.getParameter("idProject");

                if (idProjectParam != null && idProjectParam.matches("\\d+")) {
                    int projectId = Integer.parseInt(idProjectParam);
                    Project project = projectService.getById(projectId);

                    if (project == null) {
                        return;
                    }

                    boolean isLeaderOfProject = userLoggedIn.getId() == project.getUser().getId();

                    if (isAdmin || isLeaderOfProject) {
                        chain.doFilter(request, response);
                    } else {
                        BaseResponse baseResponse = new BaseResponse(200,
                                "You are not the leader of project id " + projectId, false);

                        response.setContentType("application/json; charset=UTF-8");

                        String dataJSON = gson.toJson(baseResponse);

                        PrintWriter out = response.getWriter();
                        out.print(dataJSON);
                        out.flush();

                        return;
                    }
                } else {
                    return;
                }
            } else if ("PUT".equalsIgnoreCase(httpMethod)
                    || "DELETE".equalsIgnoreCase(httpMethod)) {
                if (idParam != null && idParam.matches("\\d+")) {
                    int taskId = Integer.parseInt(idParam);
                    Task task = taskService.getById(taskId);

                    if (task == null) {
                        return;
                    }

                    boolean isLeaderOfProject = userLoggedIn.getId() == task.getProject().getUser()
                            .getId();

                    if (isAdmin || isLeaderOfProject) {
                        chain.doFilter(request, response);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
            break;

        case "/api/tasks/detail":
            if (idParam != null && idParam.matches("\\d+")) {
                int taskId = Integer.parseInt(idParam);
                Task task = taskService.getById(taskId);

                if (task == null) {
                    return;
                }

                boolean isLeaderOfProject = userLoggedIn.getId() == task.getProject().getUser()
                        .getId();

                if (isAdmin || isLeaderOfProject) {
                    chain.doFilter(request, response);
                } else {
                    return;
                }
            } else {
                return;
            }
            break;

        case "/api/tasks/no-user":
            String pidParam = req.getParameter("pid");

            if (pidParam != null && pidParam.matches("\\d+")) {
                int projectId = Integer.parseInt(pidParam);
                Project project = projectService.getById(projectId);

                if (project == null) {
                    return;
                }

                boolean isLeaderOfProject = userLoggedIn.getId() == project.getUser().getId();

                if (isAdmin || isLeaderOfProject) {
                    chain.doFilter(request, response);
                } else {
                    return;
                }
            } else {
                return;
            }
            break;

        case "/api/tasks/user":
            String uidParam = req.getParameter("uid");

            if (uidParam != null && uidParam.matches("\\d+")) {
                chain.doFilter(request, response);
            } else {
                return;
            }
            break;

        case "/api/tasks/update-status":
            String sidParam = req.getParameter("sid");

            if (idParam != null && idParam.matches("\\d+") && sidParam != null
                    && sidParam.matches("\\d+")) {
                int taskId = Integer.parseInt(idParam);
                TaskUser taskUser = taskUserService.getOne(taskId);

                if (taskUser == null) {
                    return;
                }

                boolean isLeaderOfProject = userLoggedIn.getId() == taskUser.getTask().getProject()
                        .getUser().getId();
                boolean isSameUser = userLoggedIn.getId() == taskUser.getUser().getId();

                if (isAdmin || isLeaderOfProject || isSameUser) {
                    chain.doFilter(request, response);
                } else {
                    return;
                }
            } else {
                return;
            }
            break;
        default:
            break;
        }
    }
}
