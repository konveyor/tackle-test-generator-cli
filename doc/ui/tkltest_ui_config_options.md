# TackleTest-UI Configuration Options

The configuration options for `tkltest-ui` commands  are specified in a [TOML](https://toml.io/en/)
file, containing sections for different commands and subcommands.

A subset of the configuration options can also be specified on the command line.
If an option is specified both on the command line
and in the configuration file, the command-line value overrides the configuration-file value.

The configuration file can be initialized via `tkltest-ui config init`. `tkltest-ui config list` lists
all available configuration options with information about each option: the option's TOML name,
the option's command-line short/long names (if it supported in the CLI), whether the option is
required, and the option description).

In addition to the main configuration options, TackleTest-UI uses two auxiliary configuration files (also
specified in TOML format): one for specifying input data to be used web forms of the app under test, and
the other for specifying which web elements to explore as clickables. The names of the auxiliary
configuration files are specified in the main configuration and both them are optional. Form data specification
would typically be needed for the crawler to explore the app state space effectively (e.g., for getting
past authentication form). Clickables specification serves two purposes: first, it lets the crawling scope
to be limited by instructing the crawler to avoid certain clickable elements; second, it lets the user
specify additional HTML elements to be explored that the crawler may not recognize by default as
clickable (e.g., HTML `<div>` tags).

## Main Configuration Options

The following table lists the main configuration options of TackleTest-UI.

| TOML name ("*"=req, "^"=CLI-only)   | CLI name             | Description                                                                                             |
|-------------------------------------|----------------------|---------------------------------------------------------------------------------------------------------|
| general                             |                      |                                                                                                         |
| app_name*                           |                      | name of the application being tested                                                                    |
| app_url*                            | -u/--app-url         | URL where application under test is deployed                                                            |
| config_file^                        | -cf/--config-file    | path to TOML file containing configuration options                                                      |
| log_level^                          | -l/--log-level       | logging level for printing diagnostic messages                                                          |
| test_directory                      | -td/--test-directory | name of root test directory containing the generated JUnit test classes                                 |
| verbose                             | -vb/--verbose        | run in verbose mode printing detailed status messages                                                   |
| version^                            | -v/--version         | print CLI version number                                                                                |
|                                     |                      |                                                                                                         |
| config                              |                      | Initialize configuration file or list configuration options                                             |
|                                     |                      |                                                                                                         |
| config.init                         |                      | Initialize configuration options and print (in TOML format) to file or stdout                           |
| file^                               | -f/--file            | name of TOML file to create with initialized configuration options                                      |
|                                     |                      |                                                                                                         |
| config.list                         |                      | List all configuration options with description                                                         |
|                                     |                      |                                                                                                         |
| generate                            |                      | Generate UI test cases on the application under test                                                    |
| browser                             | -b/--browser         | browser on which to launch app under test for crawling and test generation; default is chrome_headless  |
| time_limit                          | -t/--time-limit      | maximum crawl time (in minutes); default is 5 min                                                       |
| max_states                          |                      | maximum UI pages/states to discover during crawl; default is 0 (unlimited)                              |
| include_iframes                     |                      | if the AUT has iframes that need to be covered, set this option to true; default is false               |
| wait_after_event                    |                      | the time to wait (in milliseconds) after an event has been fired; default is 500ms                      |
| wait_after_reload                   |                      | the time to wait (in milliseconds) after URL load; default is 500; default is 500ms                     |
| clickables_spec_file                |                      | TOML file containing specification of elements to click or not to click                                 |
| form_data_spec_file                 |                      | TOML file containing specification of form data                                                         |
|                                     |                      |                                                                                                         |
| execute                             |                      | Execute generated UI tests on the application under test                                                |
| api_type                            | -at/--api-type       | library API type used by the generated (Java) test cases: Selenium or Crawljax; default is Selenium API |
|                                     |                      |                                                                                                         |
We provide further explanation of some of these configuration options.

### Generate command options

- `browser`: TackleTest-UI currently supports test generation on the Chrom browser only and choices for this option are `chrome` and `chrome_headless`. In the future, additional browsers, such as Firefox, will be supported.
- `time_limit`: This is used to control the total duration of crawling and is specified in minutes; the default value of 5 minutes would typically not be sufficient for adequate exploration of most webapps. This value should be set to something larger, e.g., 180 for a 3-hour crawl.
- `max_states`: Limiting the number of states to be discovered is another way, apart from time limit, for controlling app exploration. The default value is `0`, which translates to unlimited number of states. The crawler uses sophisticated state abstraction functions for determining what constitutes a new state of the app under test; for example, a search results page that can have many different concrete states or search results (depending on the search query) would be considered as one abstract state.
- `include_iframes`: If the app under test uses the `iframe` HTML tag for inlinne frames, and such frames have to be explored, this option should be set to true (default is false). Setting this option to true enables frame handling during crawling.
- `wait_after_event`: This is the duration in millisecond that the crawler waits after firing an event (i.e., performing an action such as click) on the app interface. The defgault value of 500ms generally works well, but would you might want to increase this if there are latencies in the app under test.
- `wait_after_reload`: This is the duration in millisecond that the crawler waits after reloading the app's main URL. The default value is 500ms and should be increased if tere are latencies involved in loading the app URL.

### Execute command options

- `api_type`: TackleTest-UI generates two test suites (in Java), one using Crawljax's API and the other using the Selenium API. For executing tests using the CLI, this option specified which test suite to execute; the dafault value of this option is `selenium`, and can be changed to `crawljax` to execute the Crawljax API test suite. Note thhat the test suites are created as Maven projects, so they can be executed directly via `mvn test` or in an IDE.

## Form Data Specification

The form data specification for the webapp under test is specified in a separate TOML file, whose name is
provided  as main configuration option `form_data_spec_file`. The user has to decide which web forms of the
app under  test to provide data for. At a minimum, this would be required for screens, such as
login or authentication,  so that the crawler can get past such screens and explore the app under test.
For the forms for which data is not provided, the crawler uses random values. Therefore, for the forms
where random values do not affect app exploration, form data need not be provided. Note however that the
random data used by the crawler would be of string type because the crawler cannot know whether specifically
typed data is needed for a form field (e.g., integer or date values). So, while deciding whether to provide
data for a particular form of the app under test, please assess whether random string values for form fields
that require other types of data could restrict the crawler's exploration of the app.

The form data specification is provided in TOML format in a particular schema.
For an example specification, see the [form data spec for sample Petclinic webapp](../../test/ui/data/petclinic/tkltest_ui_formdata_config.toml).

For each form, you need to specify a table with the form name `[forms.<form name>]`. In the 
table of each form, you specify a list of input data for different fields. For each field,
you need to specify (1) `input_type`, (2) `identification`, and (3) `input_value`.  
1. `input type` is either `text`, `select`, `checkbox`, `radio`, `email`, `textarea`, `password`, or `number`.
2. `identification` consists of two parts:
   1. `how`: enum with choices `name`, `id`, `tag`, `text`, `xpath`, `partial_text`
   2. `value`: string value for `how`
3. `input_value` is the string value to be used as input. 

In addition, for each form, you need to specify one `before_click` element to identify the web element that is 
clicked to submit the form. It consists of two parts:

1. `tag_name`: string specifying HTML tag
2. one of `with_attribute`, `with_text`, or `under_xpath`, taking the following structure
    ```buildoutcfg
    with_attribute = { attr_name = "", attr_value = ""}
    with_text = "<text>"
    under_xpath = "<xpath>"
    ```

To illustrate with an example, the following is a data specification for the add-owner form
in the Petclinic webapp:

```buildoutcfg
[forms.add_owner]

  [[forms.add_owner.input_fields]]
    input_type = "text"
    identification = { how = "name", value = "firstName"}
    input_value = "pipi"

  [[forms.add_owner.input_fields]]
    input_type = "text"
    identification = { how = "name", value = "lastName"}
    input_value = "yu"

  [[forms.add_owner.input_fields]]
    input_type = "text"
    identification = { how = "name", value = "address"}
    input_value = "2710 N"

  [[forms.add_owner.input_fields]]
    input_type = "text"
    identification = { how = "name", value = "city"}
    input_value = "Austin"

  [[forms.add_owner.input_fields]]
    input_type = "text"
    identification = { how = "name", value = "telephone"}
    input_value = "5122005208"

  [forms.add_owner.before_click]
    tag_name = "button"
    with_attribute = { attr_name = "value", attr_value = "Add Owner" }
```

Note that the value of an `xpath` field in the specification can be specified as absolute (or full) XPath or relative XPath. For example, for the "First Name" text field in the add-owner form in Petclinic webapp, the absolute XPath is `/html/body/app-root/app-owner-add/div/div/form/div[2]/div/input`, whereas the relative XPath is `//*[@id=“firstName”]`.


## Clickables Specification

TBD
