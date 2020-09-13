<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html>
<title>Tour Details</title>
<body>
<h1>Tour Details</h1>
Tour Details for ${tour.tourName}
<p>${tour.description}</p>
<p>Schedule:</p>
<table>
    <tr>
        <th>Venue</th>
        <th></th>
        <th>Date</th>
        <th>Days Until</th>
    </tr>
    <c:forEach var="concert" items="${tour.concerts}">
        <tr>
            <td><c:out value="${concert.venue.name}" /></td>
            <td><c:out value="${concert.type}" /></td>
            <td><fmt:formatDate pattern="dd MMM yyyy" value="${concert.date}"/></td>
            <td><c:out value="${concert.countDown}" /></td>
        </tr>
    </c:forEach>
</table>
<span>
    <c:out value="${tour.producer.name} ${tour.producer.surname}" />
</span>
<a href="../bands">Back to Band List</a>
</body>
</html>