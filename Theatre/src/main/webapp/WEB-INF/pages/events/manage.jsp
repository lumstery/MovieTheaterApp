<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head>
<title>Manage Events</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
<jsp:include page="../essentials/essentials.jsp" />
</head>
<body>
	<div id="shell">
		<jsp:include page="../header.jsp" />
		<div id="main">
			<br /> <a
				href="${pageContext.request.contextPath}/events/manage/add"
				class="btn btn-info">Add Event</a> <a href="#" class="btn btn-info">Load
				Events From File</a>
			<div id="content">
				<div class="box">
					<c:forEach items="${allEvents}" var="event">
						<div class="movie">
							<div class="movie-image">
								<span class="play"><span class="name">${event.name}</span></span>
								<a style="z-index: 9999;"
									href="${pageContext.request.contextPath}/event/${event.id}"><img
									src="${pageContext.request.contextPath}/resources/css/images/movie1.jpg"
									alt="movie" /></a>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<jsp:include page="../footer.jsp" />
	</div>
</body>
</html>