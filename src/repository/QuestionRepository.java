public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findFirstByIdGreaterThanOrderByIdAsc(Long id);
}
