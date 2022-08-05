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
| max_explore_action                  |                      | maximum number of times to explore a discovered action; default is 1                                    |
| include_iframes                     |                      | if the AUT has iframes that need to be covered, set this option to true; default is false               |
| wait_after_event                    |                      | the time to wait (in milliseconds) after an event has been fired; default is 500ms                      |
| wait_after_reload                   |                      | the time to wait (in milliseconds) after URL load; default is 500; default is 500ms                     |
| clickables_spec_file                |                      | TOML file containing specification of elements to click or not to click                                 |
| form_data_spec_file                 |                      | TOML file containing specification of form data                                                         |
| precrawl_actions_spec_file          |                      | TOML file containing specification of UI actions to be executed before crawl                            |
|                                     |                      |                                                                                                         |
| execute                             |                      | Execute generated UI tests on the application under test                                                |
| api_type                            | -at/--api-type       | library API type used by the generated (Java) test cases: Selenium or Crawljax; default is Selenium API |
|                                     |                      |                                                                                                         |
We provide further explanation of some of these configuration options.

### Generate command options

- `browser`: TackleTest-UI currently supports test generation on the Chrom browser only and choices for this option are `chrome` and `chrome_headless`. In the future, additional browsers, such as Firefox, will be supported.
- `time_limit`: This is used to control the total duration of crawling and is specified in minutes; the default value of 5 minutes would typically not be sufficient for adequate exploration of most webapps. This value should be set to something larger, e.g., 180 for a 3-hour crawl.
- `max_states`: Limiting the number of states to be discovered is another way, apart from time limit, for controlling app exploration. The default value is `0`, which translates to unlimited number of states. The crawler uses sophisticated state abstraction functions for determining what constitutes a new state of the app under test; for example, a search results page that can have many different concrete states or search results (depending on the search query) would be considered as one abstract state.
- `max_explore_action`: Number of times to explore a discovered action. The default is `1`, i.e., once. Increasing this value can be desired because CrawlJax defines an abstraction on actions, hence repeating discovered actions may lead to discovering new crawl paths when the abstraction is not accurate enough. 
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
For an example specification, see the [form data spec for the Petclinic webapp](../../test/ui/data/petclinic/tkltest_ui_formdata_config.toml).

For each form, you need to specify a table with the form name `[forms.<form name>]`. Note that the form 
name is just a placeholder that is not extracted from the DOM representation. In the 
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

A clickable is a web element on which actions (e.g., click, select, enter text) can be performed.
Some of these actions can transition the webapp from one state to another.
The clickables specification, as mentioned above, serves two purposes:
first, it lets the crawling scope to be limited by instructing the crawler to avoid certain clickable elements;
second, it lets the user specify additional HTML elements to be explored that the crawler may not recognize by
default as clickable (e.g., HTML `<div>` tags).

For most webapps, there is a huge space (often unbounded) of navigation options, some of them irrelevant to
the functionality being tested. Thus, restricting the state space of the application to be explored can make
crawling more effective by avoiding irrelevant or less interesting parts of the application. Note that if the crawler
reaches a state that is outside the domain of the URL at which the app under test is hosted, it will not explore
that state; moreover, such "external" states will not be represented in the crawl model. So the clickables
specifications need not cover such scenarios and can focus on state space within the application domain for
exclusion.

Specification of constraints on clickables is done in a separate configuration file, as in the case of data form
specification. The location of the clickables specification file should be given in the config option
`clickables_spec_file` in the main configuration file.

The clickables specification consists of `click` and `dont_click` parts, corresponding to the two usage scenarios,
that are specified in the same way---consisting of a set of element-level specifications. An element-level
specification identifies one or more web elements as follows:

1. `tag_name`: string, or a list of strings, specifying HTML tag(s)
2. optionally, one of `with_attribute`, `with_text`, or `under_xpath`, taking the following structure
    ```buildoutcfg
    with_attribute = { attr_name = "", attr_value = ""}
    with_text = "<text>"
    under_xpath = "<xpath>"
    ```
If the second part is omitted from an element specification, the click or don't click directive applies
to all occurrences of the given tag name.

It also possible to exclude an entire tree of web elements by using the `under_xpath` specifier with
a wildcard. For example, a `dont_click` specifier `under_xpath = //div[@id='xyz']//*` would exclude
all web elements in the tree rooted at the web element with id `xyz`. This can be a convenient way
of omitting exploration, for example, of web elements located in the header or footer section of a web page.
Alternatively, one can specify `dont_click.children_of`, causing the entire subtree of the element 
to not be clicked. The `dont_click.children_of` specification identifies the excluded web elements as follows:

1. `tag_name`: string, or a list of strings, specifying HTML tag(s)
2. optionally, one of `with_class` or `with_id`, taking the following structure
    ```buildoutcfg
    with_class = "<class>"
    with_id = "<id>"
    ```


To illustrate, here are a few examples of `click` specifications:

```buildoutcfg
# click all elements with tag "tag1"
[[click.element]]
  tag_name = "tag1"

# click div elements with id "is_clickable"
[[click.element]]
  tag_name = ["div"]
  with_attribute = { attr_name = "id", attr_value = "is_clickable"}

# click tag1 and tag2 elements with text "some text"
[[click.element]]
  tag_name = ["tag1", "tag2"]
  with_text = "some text"

# click div elements that occur under the matching xpath
[[click.element]]
  tag_name = "div"
  under_xpath = "//*[@id=\"primary-links\"]/li"
```

`dont_click` specifications follow the same structure, as illustrated by the following examples:

```buildoutcfg
# do not click any anchor element
[[dont_click.element]]
  tag_name = ["a"]

# do not click input elements with id "Delete records"
[[dont_click.element]]
  tag_name = ["input"]
  with_attribute = { attr_name = "id", attr_value = "Delete records"}

# do not click button elements with name "Update record..."
[[dont_click.element]]
  tag_name = "button"
  with_attribute = { attr_name = "name", attr_value = "Update record..."}

# do not click anchor elements with text "Upload file"
[[dont_click.element]]
  tag_name = "a"
  with_text = "Upload file"

# do not click div and tag123 elements that occur under the matching xpath
[[dont_click.element]]
  tag_name = ["div", "tag123"]
  under_xpath = "//*[@id=\"primary-links\"]/li"
```


`dont_click.children_of` specifications follow the same structure, as illustrated by the following examples:

```buildoutcfg
# do not click the entire top bar with the "topbar" id
[[dont_click.children_of]]
	tag_name = ["div"]
	with_id = "topbar"
```

## Pre-crawl Actions Specification

The pre-crawl specifications file can be used for specifying UI actions that should be performed on the
AUT prior to crawling. For example, the AUT may require some initial actions (e.g., dismissing a modal dialog)
to bring to a state  in which crawling can begin. The specified pre-crawl actions are added to the generated
Selenium API tests to be executed as one-time setup before test execution begins.

The form data specification is provided in TOML format in a particular schema.
For an example specification, see the [pre-crawl actions spec for the Petclinic webapp](../../test/ui/data/petclinic/tkltest_ui_precrawl_actions_config.toml).

The specification consists of a sequence of "click" (link, button) and "enter" (into text box) actions;
currently, these are the only supported pre-crawl actions. A pre-crawl action has the following
properties:

1. `action_type`: the type of action to perform (click or enter)
2. one of the following element locator properties: `by_id`, `by_name`, `under_xpath`, `with_text`, `by_css_selector`
3. if `action_type` is "enter", the property `input_value` specifying the value to be entered in text box

The following sample illustrates a sequence of five pre-crawl actions: three click actions, followed by a
form-field enter action, and finally another click action.

```
[[precrawl_action]]
  action_type = "click"
  by_id = "id"

[[precrawl_action]]
  action_type = "click"
  by_name = "name"

[[precrawl_action]]
  action_type = "click"
  under_xpath = "//*[@id=\"primary-links\"]/li"

[[precrawl_action]]
  action_type = "enter"
  by_name = "fileName"
  input_value = "some_file"

[[precrawl_action]]
  action_type = "click"
  with_text = "Upload file"
```
