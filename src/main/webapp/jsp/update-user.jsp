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
            <h4 class="page-title">Update user</h4>
          </div>
        </div>
        <!-- /.row -->
        <!-- .row -->
        <div class="row">
          <div class="col-md-2 col-12"></div>
          <div class="col-md-8 col-xs-12">
            <div class="white-box">
              <form class="form-horizontal form-material update-user-form"
                action='<c:url value="/api/users">
                <c:param name = "id" value = "${param.id}"/>
                </c:url>'>
                <div class="form-group">
                  <label class="col-md-12">Full name</label>
                  <div class="col-md-12">
                    <input type="text" placeholder="Johnathan Doe" name="fullName"
                      value="${user.fullName}" required class="form-control form-control-line">
                  </div>
                </div>
                <div class="form-group">
                  <label for="example-email" class="col-md-12">Email</label>
                  <div class="col-md-12">
                    <input type="email" placeholder="johnathan@admin.com"
                      class="form-control form-control-line" name="email" id="example-email"
                      value="${user.email}" required>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-md-12">Password</label>
                  <div class="col-md-12">
                    <input type="password" name="password" class="form-control form-control-line"
                      value="${user.password}" required>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-md-12">Phone number</label>
                  <div class="col-md-12">
                    <input type="text" placeholder="123 456 7890" name="phoneNumber"
                      value="${user.phoneNumber}" class="form-control form-control-line">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-12">Address</label>
                  <div class="col-sm-12">
                    <select name="address" class="form-control form-control-line">
                      <option ${user.address == 'Vietnam' ? 'selected' : ''}>Vietnam</option>
                      <option ${user.address == 'India' ? 'selected' : ''}>India</option>
                      <option ${user.address == 'USA' ? 'selected' : ''}>USA</option>
                      <option ${user.address == 'Canada' ? 'selected' : ''}>Canada</option>
                      <option ${user.address == 'Thailand' ? 'selected' : ''}>Thailand</option>
                    </select>
                  </div>
                </div>
                <c:if test="${userLoggedIn.role.name.equalsIgnoreCase('ADMIN')}">
                  <div class="form-group">
                    <label class="col-sm-12">Role</label>
                    <div class="col-sm-12">
                      <select name="idRole" required="required"
                        class="form-control form-control-line">
                        <option ${user.role.id == 0 ? 'selected' : ''} value="" disabled
                          hidden="hidden">Choose here</option>
                        <c:if test="${roles != null}">
                          <c:forEach items="${roles}" var="role">
                            <option value="${role.id}" ${user.role.id == role.id ? 'selected' : ''}>
                              ${role.name}</option>
                          </c:forEach>
                        </c:if>
                      </select>
                    </div>
                  </div>
                </c:if>
                <div class="form-group">
                  <div class="col-sm-12">
                    <button type="submit" class="btn btn-success">Save</button>
                    <c:if
                      test="${userLoggedIn.role.name.equalsIgnoreCase('ADMIN') || 
                        userLoggedIn.role.name.equalsIgnoreCase('LEADER')}">
                      <a href="<c:url value="/users"></c:url>" class="btn btn-primary">Return</a>
                    </c:if>
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
  <!-- Menu Plugin JavaScript -->
  <script src="plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.js"></script>
  <!--slimscroll JavaScript -->
  <script src="js/jquery.slimscroll.js"></script>
  <!--Wave Effects -->
  <script src="js/waves.js"></script>
  <!-- Custom Theme JavaScript -->
  <script src="js/custom.min.js"></script>
  <script src="js/update-user.js"></script>
</body>

</html>