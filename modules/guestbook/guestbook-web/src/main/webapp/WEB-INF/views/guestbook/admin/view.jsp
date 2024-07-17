<%--
  Created by IntelliJ IDEA.
  User: 62499
  Date: 2024-07-15
  Time: 오후 1:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/init.jsp" %>
<liferay-ui:success key="guestbookAdded" message="guestbook-added" />
<liferay-ui:success key="guestbookUpdated" message="guestbook-updated" />
<liferay-ui:success key="guestbookDeleted" message="guestbook-deleted" />

<portlet:renderURL var="editGuestbookURL">
    <portlet:param name="action" value="editGuestbook"/>
</portlet:renderURL>

<portlet:actionURL var="deleteGuestbookURL">
    <portlet:param name="action" value="deleteGuestbook"/>
</portlet:actionURL>

<script type="text/javascript" src="<c:url value='/js/modules/dropdown.js'/>"></script>

<table class="table mt-4">
    <thead>
    <tr>
        <th scope="col" class="text-center">Name</th>
        <th scope="col" class="text-center">Action</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${guestbooks.size() == 0}">
        <tr>
            <td colspan="2" class="text-center">No data was found.</td>
        </tr>
    </c:if>
    <c:forEach items="${guestbooks}" var="guestbook" varStatus="status">
        <tr>
            <td class="text-center">${guestbook.name}</td>
            <td class="">
                <div class="text-center">
                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle dropdown-toggle-ext" type="button" data-toggle="dropdown" aria-expanded="false">
                            Action
                        </button>
                        <div class="dropdown-menu" style="min-width: unset; left: 50%; transform: translateX(-50%)" data-guestbookId="${guestbook.guestbookId}">
                            <button type="button" class="edit-guestbook dropdown-item">Edit</button>
                            <button type="button" class="delete-guestbook dropdown-item">Delete</button>
                        </div>
                    </div>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<button type="button" id="add-guestbook" class="btn btn-primary mt-4">Add Guestbook</button>

<script>
    (async function () {
        /*
            Declare
        */
        const _namespace = '<portlet:namespace/>';
        const _url = {
            viewURL: '${viewURL}',
            editGuestbookURL: '${editGuestbookURL}',
            deleteGuestbookURL: '${deleteGuestbookURL}'
        }

        const _guestbookId = Number('${guestbookId}');

        const actionButtonEvent = (event) => {
            const guestbookId = event.target.parentNode.getAttribute("data-guestbookId");
            const buttonClass = event.target.classList;

            if (buttonClass.contains("edit-guestbook")) {
                performAction("editGuestbookURL", guestbookId);
            } else if (buttonClass.contains("delete-guestbook")) {
                performAction("deleteGuestbookURL", guestbookId);
            }
        };

        const performAction = (urlKey, guestbookId) => {
            const url = _url[urlKey];
            const queryParameters = [
                _namespace + "guestbookId=" + guestbookId
            ].join("&");
            location.href = url + "&" + queryParameters;
        };

        const editGuestbook = () => {
            location.href = _url.editGuestbookURL + "&" + _namespace + "guestbookId=" + _guestbookId;
        }

        /*
            Event
        */
        document.getElementById('add-guestbook').addEventListener('click', editGuestbook);

        const actionButtons = document.querySelectorAll('.edit-guestbook, .delete-guestbook');
        actionButtons.forEach((button) => {
            button.addEventListener('click', actionButtonEvent);
        });

        /*
            Initialization
        */
         new Dropdown('.dropdown-toggle-ext');
        // const dropdown = new Dropdown('.dropdown-toggle-ext');
        // dropdown.init();
    }());
</script>

