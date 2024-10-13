# Auto ANEM

**Auto ANEM** is a Java-based tool that automatically monitors the [ANEM/Minha](https://minha.anem.dz/) platform for available appointment dates.
Once a free date is detected, the program stops and notifies the user to complete the reservation manually.

**Features:**
- Monitors appointment availability with configurable intervals.
- Lightweight configuration via `auto-anem.properties`.


## Usage

- Write `auto-anem.properties` config in your home directory (`~/` on Linux or `C:/Users/YOUR_NAME/` on Windows):
```properties
numeroDemandeur: 666666666
numeroIdentification: 999999999999999

# Optional. It defaults to 15 minutes.
refreshIntervalInMinutes: 10
```

- Run `Main`.


## TODO

- [ ] User interface (TUI).

- [ ] Notifications
    - [ ] A message via Telegram or Whatsapp.
    - [ ] A toast notification (desktop).
    - [ ] A sound/alarm.

- [ ] Improve the HTTP client by adding `Accepts` and `User-Agent` headers (maybe even `Origin` and `Referer`).

- [ ] Get rid of unneeded libraries like Commons IO and maybe Commons Configuration.


## Technologies used

- [x] Apache WebComponents' `HttpClient` 5 https://hc.apache.org/

- [x] Gson https://github.com/google/gson

- [ ] Retrofit https://github.com/square/retrofit

- [x] Apache Commons - Configuration https://commons.apache.org/proper/commons-configuration/
    * https://commons.apache.org/proper/commons-configuration/apidocs/org/apache/commons/configuration2/PropertiesConfiguration.html
    * **Remarks**:
        + We could have used https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html

- [ ] Lanterna https://github.com/mabe02/lanterna
  for TUI

- [ ] Jexer https://gitlab.com/AutumnMeowMeow/jexer
  https://jexer.sourceforge.io/
  Interesting: https://hpjansson.org/chafa/
  for TUI


## Prior work

- https://github.com/mouh2020/minha_appointement_checker
    * Written in Python; uses the Telegram API.


## License

This project is licensed under the **WTFPL** (Do What The F*** You Want to Public License) by Kaito.

---

END.
