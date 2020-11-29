package com.cognizant.truyum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cognizant.truyum.model.Cart;
import com.cognizant.truyum.model.MenuItem;

public class CartDaoSqlImpl implements CartDao {
	PreparedStatement st = null;
	ResultSet rs = null;

	MenuItemDaoSqlImpl dao;

	@Override
	public void addCartItem(long userId, long menuItemId) throws Exception {
		String query = "INSERT INTO CART (?,?)";
//		MenuItem menuItem = dao.getMenuItem(menuItemId);

		st = ConnectionHandler.getConnection().prepareStatement(query);
		st.setLong(1, userId);
		st.setLong(2, menuItemId);

	}

	@Override
	public List<MenuItem> getAllCartItems(long userId) throws CartEmptyException {
		float total = 0;
		List<MenuItem> menuItemList = new ArrayList<MenuItem>();

		Cart cart = new Cart(menuItemList, total);

		String query = "select m.menuItemId, m.title, m.isFreeDelivery, m.price from users u\r\n"
				+ "join cart c on u.user_id = c.user_id\r\n"
				+ "join menu_item m on c.menuItem_id = m.menuItemId where u.userId = " + userId;
		try {
			st = ConnectionHandler.getConnection().prepareStatement(query);
			rs = st.executeQuery();
			
			if (rs.next() == false) {
				throw new CartEmptyException();
			}
			
			while (rs.next()) {
				MenuItem menuItem = new MenuItem();
				menuItem.setId(rs.getLong(1));
				menuItem.setName(rs.getString(2));
				menuItem.setFreeDelivery(rs.getBoolean(3));
				menuItem.setPrice(rs.getFloat(4));
				menuItemList.add(menuItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void removeCartItem(long userId, long menuItemId) {
		// TODO Auto-generated method stub

	}

}
