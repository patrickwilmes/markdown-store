package com.bitlake.backend.user.persistence;

import com.bitlake.backend.generated.Tables;
import com.bitlake.backend.generated.tables.records.UsersRecord;
import com.bitlake.backend.user.User;
import com.bitlake.backend.user.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JooqUserRepository implements UserRepository {

  private final DSLContext dsl;

  public JooqUserRepository(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  public User getByUsername(String username) {
    Result<UsersRecord> fetch =
      dsl.selectFrom(Tables.USERS).where(Tables.USERS.USERNAME.eq(username)).fetch();
    if (fetch.size() == 1) {
      return new User(fetch.getFirst().getUsername(), fetch.getFirst().getPassword());
    }
    return null;
  }

  @Override
  public User register(User user) {
    dsl.insertInto(Tables.USERS).set(Tables.USERS.USERNAME, user.username())
      .set(Tables.USERS.PASSWORD, user.hashedPassword()).execute();
    return getByUsername(user.username());
  }
}
