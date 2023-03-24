package chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MoveDaoImpl implements MoveDao {
    public void saveAll(final QueryStrategy saveStrategy) {
        final String insertQuery = "INSERT INTO move (source, target) VALUES (?, ?)";
        connectDataBaseAndDoQuery(insertQuery, saveStrategy);
    }

    private void connectDataBaseAndDoQuery(final String query, final QueryStrategy strategy) {
        try (
                final Connection connection = ConnectionGenerator.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            strategy.save(preparedStatement);
        } catch (final SQLException e) {
            throw new IllegalStateException("데이터 베이스에 쿼리를 보낼 수 없습니다.");
        }
    }

    @Override
    public List<Move> findAll(final MoveFindAllStrategy findAllStrategy) {
        final String findAllQuery = "SELECT source, target FROM move";
        return connectDataBaseAndFind(findAllQuery, findAllStrategy, new MoveMapper());
    }

    private List<Move> connectDataBaseAndFind(
            final String query,
            final QueryStrategy strategy,
            final RowMapper<Move> rowMapper
    ) {
        try (
                final Connection connection = ConnectionGenerator.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            return strategy.findAll(resultSet, rowMapper);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
