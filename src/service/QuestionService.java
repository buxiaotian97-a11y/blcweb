@Service
public class QuestionService {
    private final QuestionRepository repo;

    public QuestionService(QuestionRepository repo) { this.repo = repo; }

    public QuestionDto findNext(Long after) {
        // 超シンプル版：ID昇順で次の1件
        return repo.findFirstByIdGreaterThanOrderByIdAsc(
                 after == null ? 0L : after
               ).map(QuestionDto::from).orElseThrow();
    }

    public QuestionDto getOne(Long id) {
        return repo.findById(id).map(QuestionDto::from).orElseThrow();
    }
}