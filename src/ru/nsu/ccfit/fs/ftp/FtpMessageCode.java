package ru.nsu.ccfit.fs.ftp;

/*
100 	Запрошенное действие инициировано, дождитесь следующего ответа прежде, чем выполнять новую команду.
110 	Комментарий
120 	Функция будет реализована через nnn минут
125 	Канал открыт, обмен данными начат
150 	Статус файла правилен, подготавливается открытие канала
200 	Команда корректна
202 	Команда не поддерживается
211 	Системный статус или отклик на справочный запрос
212 	Состояние каталога
213 	Состояние файла
214 	Справочное поясняющее сообщение
215 	Выводится вместе с информацией о системе по команде SYST
220 	Слишком много подключений к FTP-серверу (можете попробовать позднее). В некоторых версиях указывает на успешное завершение промежуточной процедуры
221 	Благополучное завершение по команде quit
225 	Канал сформирован, но информационный обмен отсутствует
226 	Закрытие канала, обмен завершен успешно
227 	Переход в пассивный режим (h1,h2,h3,h4,p1,p2).
228 	переход в длинный пассивный режим (длинный адрес, порт).
229 	Переход в расширенный пассивный режим (|||port|).
230 	Пользователь идентифицирован, продолжайте
231 	Пользовательский сеанс окончен; Обслуживание прекращено.
232 	Команда о завершении сеанса принята, она будет завершена по завершении передачи файла.
250 	Запрос прошёл успешно
257 	«ПУТЬ» создан.
331 	Имя пользователя корректно, нужен пароль
332 	Для входа в систему необходима аутентификация
350 	Запрошенное действие над файлом требует большей информации
404 	Данный удалённый сервер не найден
421 	Процедура не возможна, канал закрывается
425 	Открытие информационного канала не возможно
426 	Канал закрыт, обмен прерван
434 	Запрашиваемый хост недоступен
450 	Запрошенная функция не реализована, файл не доступен, например, занят
451 	Локальная ошибка, операция прервана
452 	Ошибка при записи файла (недостаточно места)
500 	Синтаксическая ошибка, команда не может быть интерпретирована (возможно она слишком длинна)
501 	Синтаксическая ошибка (неверный параметр или аргумент)
502 	Команда не используется (нелегальный тип MODE)
503 	Неудачная последовательность команд
504 	Команда не применима для такого параметра
530 	Вход не выполнен! Требуется авторизация (not logged in)
532 	Необходима аутентификация для запоминания файла
550 	Запрошенная функция не реализована, файл не доступен, например, не найден
551 	Запрошенная операция прервана. Неизвестный тип страницы.
552 	Запрошенная операция прервана. Выделено недостаточно памяти
553 	Запрошенная операция не принята. Недопустимое имя файла.
*/
public enum FtpMessageCode {
    //WAIT(100),
    COMMENT(110),
    TRANSFER_STARTING(125),
    OKAY_FILE_STATUS(150),
    OKAY(200),
    COMMAND_NOT_IMPLEMENTED(202),
    FILE_STATUS(213),
    HELP_MESSAGE(214),
    CONNECTED(220),
    SERVICE_CLOSING_CONTROL_CONNECTION(221),
    DATA_CONNECTION_OPEN(225),
    CLOSING_DATA_CONNECTION(226),
    ENTERING_PASSIVE_MODE(227),
    ENTERING_LONG_PASSIVE_MODE(228),
    ENTERING_EXTENDED_PASSIVE_MODE(229),
    //ANON_USER_LOGIN_OK(230),

    REQUESTED_FILE_ACTION_OKAY(250),
    PATHNAME_CREATED(257),
    //USERNAME_OKAY_NEED_PASSWORD(331),

    ;
    public  static final int   WAIT =100;
    public static final int  ANON_USER_LOGIN_OK = 230;
    public static final int   USERNAME_OKAY_NEED_PASSWORD = 331;

    private final int code;
    public int getCode(){
        return code;

    }
    FtpMessageCode(int code) {
        this.code = code;
    }
}
/*
 100 	Series: The requested action is being initiated, expect another reply before proceeding with a new command.
110 	Restart marker replay . In this case, the text is exact and not left to the particular implementation; it must read: MARK yyyy = mmmm where yyyy is User-process data stream marker, and mmmm server's equivalent marker (note the spaces between markers and "=").
120 	Service ready in nnn minutes.
125 	Data connection already open; transfer starting.
150 	File status okay; about to open data connection.
200 	command okay.
202 	command not implemented, superfluous at this site.
211 	System status, or system help reply.
212 	Directory status.
213 	File status.
214 	Help message.On how to use the server or the meaning of a particular non-standard command. This reply is useful only to the human user.
215 	NAME system type. Where NAME is an official system name from the registry kept by IANA.
220 	Service ready for new user.
221 	Service closing control connection.
225 	Data connection open; no transfer in progress.
226 	Closing data connection. Requested file action successful (for example, file transfer or file abort).
227 	Entering Passive Mode (h1,h2,h3,h4,p1,p2).
228 	Entering Long Passive Mode (long address, port).
229 	Entering Extended Passive Mode (|||port|).
230 	User logged in, proceed. Logged out if appropriate.
231 	User logged out; service terminated.
232 	Logout command noted, will complete when transfer done.
250 	Requested file action okay, completed.
257 	"PATHNAME" created.
331 	User name okay, need password.
332 	Need account for login.
350 	Requested file action pending further information
421 	Service not available, closing control connection. This may be a reply to any command if the service knows it must shut down.
425 	Can't open data connection.
426 	Connection closed; transfer aborted.
430 	Invalid username or password
434 	Requested host unavailable.
450 	Requested file action not taken.
451 	Requested action aborted. Local error in processing.
452 	Requested action not taken. Insufficient storage space in system.File unavailable (e.g., file busy).
500 	Syntax error, command unrecognized. This may include errors such as command line too long.
501 	Syntax error in parameters or arguments.
502 	command not implemented.
503 	Bad sequence of commands.
504 	command not implemented for that parameter.
530 	Not logged in.
532 	Need account for storing files.
550 	Requested action not taken. File unavailable (e.g., file not found, no access).
551 	Requested action aborted. Page type unknown.
552 	Requested file action aborted. Exceeded storage allocation (for current directory or dataset).
553 	Requested action not taken. File name not allowed.
631 	Integrity protected reply.
632 	Confidentiality and integrity protected reply.
633 	Confidentiality protected reply.
*/