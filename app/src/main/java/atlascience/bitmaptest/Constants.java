package atlascience.bitmaptest;

/**
 * Created by Ilyas on 15-Apr-17.
 */

public class Constants {
    public class Profile{
        public static  final String UPLOAD_URL ="https://api.codfi.ru/v1/upload.php";
    }
    public class URLS{
        public static final String BASE_URL = "https://api.codfi.ru/";
        public static final String TOPIC_CONTENT_URL ="https://learn.codfi.ru/";
        public static final String QUESTION_URL ="https://code.codfi.ru/";
        public static final String GAME_PROFILE_URL ="https://api.codfi.ru/users/";

    }

    public class TrainMode{

       public class Difficulty{
           public static final String EASY = "1";
           public static final String MEDIUM = "2";
           public static final String HARD = "3";
       }

       public class Methods{
           public static final String GET_CODE_BY_TOPIC = "get_code_by_topic";
           public static final String ANSWERS_GET_ALL = "answer_get_all";
           public static final String GET_CODE_INFO = "get_code_info";
       }
    }

    public class Methods{

        public class Version{
            public static final String VERSION = "1";
            public static final String VERSION2 = "2";
        }

        public class  User{
            /* VERSION 1 */
            public static final String ADD = "addUser";
            public static final String GET = "getUser";
            public static final String GET_ALL_BY_RATING = "getAllUserByRating";
            public static final String UPDATE = "updateUser";
            public static final String DELETE = "deleteUser";
            public static final String RESET_PASSWORD = "forgotPassword";
            public static final String SET_TOKEN = "set_token";
            public static final String UPDATE_URL = "update_url";

            /* VERSION 2 */
            public static final String SIGN_IN = "sign_in";
        }
        public class Game {

            public class Queue{
                /* VERSION 1 */
                public static final String ADD = "add_to_queue";
                public static final String DELETE = "delete_from_queue";
                public static final String GET = "get_all_from_queue";

                /* VERSION 2 */
            }

            public class Question{
                /* VERSION 1 */
                public static final String GET = "get_question";

                /* VERSION 2 */
            }
            /* VERSION 1 */
            public static final String PLAY = "go_to_game";
            public static final String INVITE = "invite_to_game";
            public static final String SET_ANSWER1 = "set_answer_one";
            public static final String SET_ANSWER2 = "set_answer_two";
            public static final String GET_ANSWER_TIME = "get_answer_time";
            public static final String REQUEST_GAME = "request_game";
            public static final String APPROVED_REQUEST = "approved_request";
            public static final String GET_RESULTS = "get_game_results";
            public static final String SET_ZONES = "set_zones";
            public static final String GET_ZONES = "get_zones";


            /* VERSION 2 */

        }

        public class Content{
            /* VERSION 1 */
            public static final String PROVIDE_QUESTION = "provide_question";
            public static final String GET_STATISTICS = "get_all_stats";

            /* VERSION 2 */
            public static final String GET_OPENED_KNOWLEDGE = "get_opened_knowledge";
            public static final String GET_ONLY_OPENED_KNOWLEDGE = "get_only_opened_knowledge";
            public static final String OPEN_KNOWLEDGE = "open_knowledge_topic";
            public static final String GET_KNOWLEDGE_IDS = "get_knowledge_ids";
        }
    }

}
