<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
table{
  border: 1px solid gray;
        width: 400px;
        margin: 0 auto; /* 테이블을 수평 가운데 정렬 */
   
}


table, tr, td {
	border: 1px solid gray;
	text-align: left;
	align-items: left;
}
.button{
border: none;
}
input{
flaot:left;
}
tr,td{
text-align: left;
}
</style>
</head>
<body>
<div>
	<jsp:include page="top.jsp" />
			<h2>상품등록</h2>
	<form action="/shop_product_insert_ok" method="post"	encType="multipart/form-data">
		<table>
			<thead>
				<tr>
					<td><label>분류</label></td>
					<td>
					<input type="radio" name="category" value="com001" required>컴퓨터 
					<input type="radio" name="category" value="ele002">가전 제품 
					<input type="radio" name="category" value="sp003">스포츠
					</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><label>제품번호</label></td>
					<td><input type="text" name="p_num"required></td>
				</tr>
				<tr>
					<td><label>제품명</label></td>
					<td><input type="text" name="p_name"required></td>
				</tr>
				<td><label>판매사</label></td>
				<td><input type="text" name="p_company"required></td>
				</tr>
				<tr>
					<td><label>상품가격</label></td>
					<td><input type="text" name="p_price"required></td>
				</tr>
				<td><label>할인가</label></td>
				<td><input type="text" name="p_saleprice"required></td>
				</tr>
				<tr>
					<td><label>상품이미지s</label></td>
					<td><input type="file" name="file_s"required></td>
				</tr>
				<tr>
					<td><label>상품이미지L</label></td>
					<td><input type="file" name="file_l"required></td>
				</tr>
				<tr>
					<td colspan="2"><label>상품내용</label></td>
				</tr>
				<tr>
					<td colspan="2"><textarea name="p_content" rows="10"
							cols="50"required></textarea></td>
				</tr>
				<tr class="button">
					<td class="button"><input type="submit" value="상품등록"></button></td>
				</tr>
			</tbody>

		</table>
	</form>
	</div>
</body>
</html>