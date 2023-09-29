<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" type="image/png" sizes="16x16" href="plugins/images/favicon.png">
<title>Pixel Admin</title>
<!-- Bootstrap Core CSS -->
<link href="bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Menu CSS -->
<link href="plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.css" rel="stylesheet">
<!-- multiple-select -->
<link rel="stylesheet" href="https://unpkg.com/multiple-select@1.6.0/dist/multiple-select.min.css">
<!-- animation CSS -->
<link href="css/animate.css" rel="stylesheet">
<!-- Custom CSS -->
<link href="css/style.css" rel="stylesheet">
<!-- color CSS -->
<link href="css/colors/blue-dark.css" id="theme" rel="stylesheet">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>

<body>
  <!-- Preloader -->
  <div class="preloader">
    <div class="cssload-speeding-wheel"></div>
  </div>
  <div id="wrapper">
    <t:nav></t:nav>

    <!-- Page Content -->
    <div id="page-wrapper">
      <div class="container-fluid">
        <div class="row bg-title">
          <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
            <h4 class="page-title">Create Task</h4>
          </div>
        </div>
        <!-- /.row -->
        <!-- .row -->
        <div class="row">
          <div class="col-md-2 col-12"></div>
          <div class="col-md-8 col-xs-12">
            <div class="white-box">
              <form class="form-horizontal form-material create-task-form"
                action='<c:url value="/api/tasks"></c:url>' method="POST">
                <div class="form-group" style="overflow: visible;">
                  <label class="col-md-12">Project</label>
                  <div class="col-md-12">
                    <select id="project-select" name="idProject"
                      class="form-control form-control-line" required>
                    </select>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-md-12">Task name</label>
                  <div class="col-md-12">
                    <input type="text" placeholder="Task name" name="name"
                      class="form-control form-control-line" required>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-md-12">Description</label>
                  <div class="col-md-12">
                    <input type="text" placeholder="Description" name="description"
                      class="form-control form-control-line">
                  </div>
                </div>
                <div class="form-group" style="overflow: visible;">
                  <label class="col-md-12">User</label>
                  <div class="col-md-12">
                    <select class="form-control form-control-line" id="user-select" name="idUser">
                    </select>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-md-12">Start date</label>
                  <div class="col-md-12">
                    <input type="date" placeholder="dd/MM/yyyy" name="startDate" id="startDate"
                      class="form-control form-control-line">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-md-12">End date</label>
                  <div class="col-md-12">
                    <input type="date" placeholder="dd/MM/yyyy" name="endDate" id="endDate"
                      class="form-control form-control-line">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-12">Status</label>
                  <div class="col-sm-12">
                    <select name="idStatus" class="form-control form-control-line">
                      <c:if test="${statuses != null}">
                        <c:forEach items="${statuses}" var="status">
                          <option value="${status.id}">${status.name}</option>
                        </c:forEach>
                      </c:if>
                    </select>
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-sm-12">
                    <button type="submit" class="btn btn-success">Save</button>
                    <a href="<c:url value="/tasks"></c:url>" class="btn btn-primary">Return</a>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="col-md-2 col-12"></div>
        </div>
        <!-- /.row -->
      </div>
      <!-- /.container-fluid -->
      <footer class="footer text-center"> 2018 &copy; myclass.com </footer>
    </div>
    <!-- /#page-wrapper -->
  </div>
  <!-- /#wrapper -->
  <!-- jQuery -->
  <script src="plugins/bower_components/jquery/dist/jquery.min.js"></script>
  <!-- Bootstrap Core JavaScript -->
  <script src="bootstrap/dist/js/bootstrap.min.js"></script>
  <!-- multiple-select -->
  <script src="https://unpkg.com/multiple-select@1.6.0/dist/multiple-select.min.js"></script>
  <!-- Menu Plugin JavaScript -->
  <script src="plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.js"></script>
  <!--slimscroll JavaScript -->
  <script src="js/jquery.slimscroll.js"></script>
  <!--Wave Effects -->
  <script src="js/waves.js"></script>
  <!-- Custom Theme JavaScript -->
  <script src="js/custom.min.js"></script>
  <script type="module" src="js/create-task.js"></script>
</body>

</html>