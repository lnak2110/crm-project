<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Tell the browser to be responsive to screen width -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<!-- Favicon icon -->
<link rel="icon" type="image/png" sizes="16x16" href="plugins/images/favicon.png">
<title>Pixel Admin</title>
<!-- Bootstrap Core CSS -->
<link href="bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Menu CSS -->
<link href="plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.css" rel="stylesheet">
<!-- Animation CSS -->
<link href="css/animate.css" rel="stylesheet">
<!-- multiple-select -->
<link rel="stylesheet" href="https://unpkg.com/multiple-select@1.6.0/dist/multiple-select.min.css">

<!-- Custom CSS -->
<link href="css/style.css" rel="stylesheet">
<!-- color CSS you can use different color css from css/colors folder -->
<!-- We have chosen the skin-blue (blue.css) for this starter
          page. However, you can choose any other skin from folder css / colors .
-->
<link href="css/colors/blue-dark.css" id="theme" rel="stylesheet">
<link rel="stylesheet" href="./css/custom.css">
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
          <div class="col-lg-9 col-sm-8 col-md-8 col-xs-12"
            style="display: flex; justify-content: flex-start;">
            <ol class="breadcrumb">
              <li>
                <a href="<c:url value="/projects"></c:url>">Projects</a>
              </li>
              <li>
                <a class="active" href="#">${project.name}</a>
              </li>
            </ol>
          </div>
          <div class="col-lg-3 col-sm-8 col-md-8 col-xs-12"
            style="display: flex; justify-content: flex-end;">
            <button class="btn btn-outline-secondary" data-toggle="modal"
              data-target="#example-modal-modal">Members</button>
            <div class="modal fade" id="example-modal-modal">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                      <span aria-hidden="true">×</span>
                      <span class="sr-only">Close</span>
                    </button>
                  </div>
                  <div class="modal-body">
                    <form class="add-members-form">
                      <div class="form-group row">
                        <label style="padding-bottom: 10px">Add members to this project</label>
                        <div>
                          <select id="add-members-select" name="newMembers" multiple required
                            style="width: 100%">
                          </select>
                        </div>
                      </div>
                      <div class="form-group row">
                        <button type="submit" class="btn btn-primary add-members-btn">Confirm</button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- /.col-lg-12 -->
        </div>
        <!-- BEGIN THỐNG KÊ -->
        <div class="row percents"></div>
        <!-- END THỐNG KÊ -->

        <!-- BEGIN DANH SÁCH CÔNG VIỆC -->
        <div class="tasks-no-user"></div>
        <div class="tasks-users"></div>
        <!-- END DANH SÁCH CÔNG VIỆC -->
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
  <script src="https://cdn.jsdelivr.net/npm/dayjs@1/dayjs.min.js"></script>
  <script src="js/project-detail.js"></script>
</body>

</html>