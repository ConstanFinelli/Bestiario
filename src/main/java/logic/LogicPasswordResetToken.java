package logic;

import data.DataPasswordResetToken;
import entities.PasswordResetToken;
import exceptions.DataNotFoundException;

public class LogicPasswordResetToken {

	private DataPasswordResetToken tokenDAO = new DataPasswordResetToken();

	public void save(PasswordResetToken token) {
		tokenDAO.save(token);
	}

	public PasswordResetToken getOne(String token) throws DataNotFoundException {
		return tokenDAO.getOne(token);
	}

	public void markAsUsed(String token) {
		tokenDAO.markAsUsed(token);
	}

	public void deleteByUser(int idUser) {
		tokenDAO.deleteByUser(idUser);
	}
}
