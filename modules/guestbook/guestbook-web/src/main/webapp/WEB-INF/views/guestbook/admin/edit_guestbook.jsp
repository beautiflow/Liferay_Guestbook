<%--
  Created by IntelliJ IDEA.
  User: 62499
  Date: 2024-07-15
  Time: 오후 5:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/init.jsp" %>

<portlet:renderURL var="viewURL">
    <portlet:param name="action" value="view" />
</portlet:renderURL>

<portlet:actionURL var="addGuestbookURL">
    <portlet:param name="action" value="addGuestbook" />
</portlet:actionURL>

<portlet:actionURL var="updateGuestbookURL">
    <portlet:param name="action" value="updateGuestbook" />
</portlet:actionURL>

<form id="form" name="<portlet:namespace/>form">
    <input type="hidden" id="guestbookId" name="<portlet:namespace/>guestbookId" class="form-control" value="${guestbook.guestbookId}">
    <div class="form-group">
        <label for="name">Name</label>
        <input type="text" id="name" name="<portlet:namespace/>name" class="form-control" value="${guestbook.name}">
    </div>
    <button type="button" id="form-submit" class="btn btn-primary">Submit</button>
    <button type="button" id="form-cancel" class="btn btn-default">Cancel</button>
</form>

<script>
    (async function () {
        /*
            Declare
        */
        const _url = {
            baseURL: '${pageContext.request.contextPath}',
            viewURL: '${viewURL}',
            submitGuestbookURL: '${guestbook eq null ? addGuestbookURL : updateGuestbookURL}',
        }

        const submitForm = () => {
            const form = document.getElementById('form');
            form.method = 'POST';
            form.action = _url.submitGuestbookURL;
            form.submit();
        }
        const cancelForm = () => {
            location.href = '${viewURL}';
        }

        /*
            Event
        */
        const submitButton = document.getElementById('form-submit');
        submitButton.addEventListener('click', submitForm);

        const cancelButton = document.getElementById('form-cancel');
        cancelButton.addEventListener('click', cancelForm);
    }());
</script>

