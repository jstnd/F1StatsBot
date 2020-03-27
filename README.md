## 🏁 F1StatsBot 🏁
F1StatsBot is a Discord bot that allows you to look up various Formula One statistics through simple commands.  
Built using the [Java Discord API (JDA)](https://github.com/DV8FromTheWorld/JDA) and [JDA-Utilities](https://github.com/JDA-Applications/JDA-Utilities), utilizing data from the [Ergast Developer API](http://ergast.com/mrd/).

## Commands
`[ ]` = optional; `< >` = required
- **`.f1 help [command]`** - Displays a help guide for the command specified. If no command is specified, a list of all commands available will be displayed instead.
    <details><summary>Example Usage</summary>
   
    ![Help Command](screenshots/help_command.png)
    </details>
- **`.f1 circuit <circuit_id>`** - Displays information about the circuit with the specified id.
    <details><summary>Example Usage</summary>
   
    ![Circuit Command](screenshots/circuit_command.png)
    </details>
- **`.f1 circuits [page]`** - Displays a list of circuits and their id's for reference purposes.
    <details><summary>Example Usage</summary>
   
    ![Circuits Command](screenshots/circuits_command.png)
    </details>
- **`.f1 constructor <constructor_id>`** - Displays information about the constructor with the specified id.
    <details><summary>Example Usage</summary>
   
    ![Constructor Command](screenshots/constructor_command.png)
    </details>
- **`.f1 constructors [page]`** - Displays a list of constructors and their id's for reference purposes.
    <details><summary>Example Usage</summary>
   
    ![Constructors Command](screenshots/constructors_command.png)
    </details>
- **`.f1 driver <driver_id>`** - Displays information about the driver with the specified id.
    <details><summary>Example Usage</summary>
   
    ![Driver Command](screenshots/driver_command.png)
    </details>
- **`.f1 drivers [page]`** - Displays a list of drivers and their id's for reference purposes.
    <details><summary>Example Usage</summary>
   
    ![Drivers Command](screenshots/drivers_command.png)
    </details>
- **`.f1 next`** - Displays information about the next Formula One race.
    <details><summary>Example Usage</summary>
   
    ![Next Command](screenshots/next_command.png)
    </details>
- **`.f1 qualifying [<season> <round>]`** - Displays qualifying results for the race specified by `<round>` in the specified `<season>`. If no race is given, the qualifying results of the previous race will be displayed.
    <details><summary>Example Usage</summary>
   
    ![Qualifying Command v1](screenshots/qualifying_command.png)
    </details>
- **`.f1 race [<season> <round>]`** - Displays driver results for the race specified by `<round>` in the specified `<season>`. If no race is given, the driver results of the previous race will be displayed.
    <details><summary>Example Usage</summary>
   
    ![Race Command](screenshots/race_command.png)
    </details>
- **`.f1 schedule [season]`** - Displays the race schedule for the specified `[season]`. If no season is specified, the schedule of the current season will be displayed.
    <details><summary>Example Usage</summary>
   
    ![Schedule Command](screenshots/schedule_command.png)
    </details>
- **`.f1 season [season]`** - Displays the constructor and driver standings for the specified `[season]`. If no season is specified, the constructor and driver standings of the current season will be displayed.
    <details><summary>Example Usage</summary>
   
    ![Season Command](screenshots/season_command.png)
    </details>