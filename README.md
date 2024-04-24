# AquaCare

[[_TOC_]]

<!---

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Set auto-merge](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing (SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***


## Suggestions for a good README

Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.
-->


## Description

AquaCare offers an aquarium monitoring system designed to assist in maintaining fish and plant life in a well-nurtured environment. AquaCare collects data using pH and temperature sensors to provide users with detailed historical data on temperature and pH levels.

AquaCare offers products tailored to owners with specific needs, including species highly sensitive to temperature and pH fluctuations. Additionally, it provides a solution for anyone who wants to be notified of a drop in water level or excessive light in the aquarium, both of which can be harmful to aquatic life.

To save time and ensure proper care, AquaCare notifies users when monitored levels (such as temperature, light or pH) exceed set thresholds, indicating the need for adjustments to protect aquatic life from hazardous conditions. AquaCare also includes an automated food dispenser for customers who are unable to manually feed the fish. The dispenser will be equipped with LED indicators to alert users of changes in conditions when the application is out of reach.

AquaCare's historical data can be used to offer potential buyers detailed insights into how fish and plants have been cared for, displaying their habitat conditions during ownership.

Our application serves as a central hub for aquarium monitoring, offering real-time sensor readings and a comprehensive overview of the health and conditions of aquatic life.

<!---
AquaCare offers an aquarium monitoring system designed to assist in maintaining fish and plant life in a well-nurtured environment. AquaCare collects data using a suite of sensors which is then stored and displayed on the user device. 

To save time and ensure proper care, AquaCare notifies users when monitored levels (such as temperature, light, water level, or pH) exceed set thresholds, indicating the need for adjustments to protect aquatic life from hazardous conditions.
AquaCare also includes an automated food dispenser for customers who are unable to manually feed the fish. 
-->
<!--- 
## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.
-->

<!---
## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.
-->

## Installation
### Prerequisites
Before installing the application, ensure you have the following prerequisites installed on your system:

Java Development Kit (JDK) version 17 or higher: [Download JDK](https://www.oracle.com/java/technologies/downloads/#java17)

### Downloading the application
To download the application, check [Relases](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/releases) tab. There you can download the most recent relase. 

After downloading the .zip file, extract the contents using [7zip](https://www.7-zip.org/)

Running the .jar or .exe file should start the application.

### Downloading the source code

To download the source code, check [Relases](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/releases) tab. There, under the assets menu, you can find and download the most recent source code relase. 

After downloading the .zip file, extract the contents using [7zip](https://www.7-zip.org/)
<!---
#### How To Compile From Source Code

!WIP

## Usage

!WIP


Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.


Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.
-->
## Roadmap
For the future releases and upcoming features, refer to [Milestones](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/milestones).

## Contributing
For contributions that everyone needs to abide by, refer [Contributions.md](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/blob/main/CONTRIBUTING.md)

## Authors and acknowledgment
- Ahmet Yavuz Kalkan([@ahmety](https://git.chalmers.se/ahmety)): Making contributions to back-end of the application, specifically app-terminal connection and database connection.

- Süeda Nalan Tahtaci([@sueda](https://git.chalmers.se/sueda)): Making contributions on the java application user interface.

- Bouali Boujerad([@bouali](https://git.chalmers.se/bouali)): Making contributions on the java applications user interface.

- Ravi Sharma([@ravisha](https://git.chalmers.se/ravisha)): Making contributions on back-end and front-end aspects of the wio terminal. Handling from connectivity to UI of the terminal.
<!--- add your contributions here without too much detail-->


<!---
## Support
[Buy us a coffee](https://www.coop.se/handla/varor/dryck/kaffe/bryggkaffe/bryggkaffe-mellanrost-8711000530085)
-->

## License
This project is under MIT license, to read more refer to [License](https://git.chalmers.se/courses/dit113/2024/group-16/dev-team-16/-/blob/main/LICENSE.MD)

<!--- 
## Project status
Project is under heavy developement
-->
