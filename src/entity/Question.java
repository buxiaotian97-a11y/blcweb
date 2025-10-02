@Entity @Table(name = "questions")
public class Question {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
  String qtext;
  String category;
  int point;
  boolean active;
  // getter/setter
}