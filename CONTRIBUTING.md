## Table of Contents

- Introduction 
     - Labels
     - Features Branch Milestones
     - Requirement
* How to Contribute
   - Isssue Creation 
   - Example Of A Good Issue
   - Commit Messages
   - Branch Creation
   - Merge Requests
   - Merge Request Example
* Contact Info


## Introduction

### Labels:
Labels categorize issues and merge requests for better organization and filtering. They indicate the nature of the work, such as priority or type, helping contributors understand and prioritize tasks. When creating an issue or merge request, select appropriate labels from the predefined list.

### Features Branch
When adding new contributions to the project, use features branches. To make changes or additions for a specific issue, first use the corresponding branch. Then, merge these changes into the main branch through a merge request.

### Milestones:
Milestones are used in relation to issues and merge requests to track progress towards specific goals or releases. They provide a high-level overview of the project's roadmap and assist in planning work.
 
### Requirements 
System requirements can be documented either in SRS format or as user epics. These epics can then be broken down into smaller user stories, each addressing a specific issue.

## How to Contribute

### Issue Creation:
1. **Issue Template**: Ensure your issue template includes a user story, description and acceptance criteria.
2. **Assignee**: Assign yourself as the assignee.
3. **Labels**: Add appropriate labels like "bug" or "improvement" as well as priority.
4. **Due Date**: Set an appropriate due date respecting the milestone's deadline.
5. **Continuous Updates**: Continuously update acceptance criteria based on committed implementations.

### Example Of A Good Issue
**Title:** Update README.md Based On Current State Of The Project

**Description:**
The README file contains outdated information regarding the current state of the project. This issue aims to update/enhance the README file to make it more informative and user-friendly.

**Acceptance Criteria**
1. **Update Project Description:** Review and update the project description to explain its current project purpose and goals. Some of the sentences may be outdated.
2. **Installation Guide:** Add detailed instructions on how to install and set up the project locally.
3. **Usage Instructions:** Provide clear guidance on how to use the project, including any necessary configurations or commands.
4. **Formatting and Styling:** Ensure consistent formatting, styling, and readability throughout the README file.
5. Update the current system diagram to adjust with the project scope and changes.
6. Explains the "Purpose and Benefits" about the project
7. Update contributions section

**Status Labels:**
- Initial status: `Documentation Improvement Medium Priority To Do`
- Upon completion: `Documentation Improvement Medium Priority In progress`

**Assignee and Reviewer:**
- **Assignee:** @ahmety


### Commit Messages:
1. **Traceability**: Include the corresponding issue via #<n> syntax.
2. **Description**: Provide a description covering the changes of the commit.

### Branch Creation:
1. **Distinct Branch**: Create a distinct branch from the corresponding issue.
2. **Branch Name**: Give the branch a concise name and enter the associated issue number as a prefix.

### Merge Requests:
1. **Assignee and Reviewer**: Ensure a merge request has an assignee (author of the issue) and a reviewer.
2. **Reviewer Duties**: Review code for quality standards, provide feedback, and approve if suitable.
3. **Status Labels**: Remove 'needs-review' label upon approval and add done label.
4. **Merging**: Only merge the request after these steps have been completed and the pipeline has passed.

### Merge Request Example:

**Assignee and Reviewer:**
- **Assignee:** @ahmety
- **Reviewer:** @ravisha

**Description:**
Resolve #30 ("Customer Request - Improvements Over Line Charts")

**Changes Made:**
- Added chart initialization
- Implemented Data management
- Created tooltips and timestamps 

**Status Labels:**
- Initial status: `Feature, High Priority, Improvement , In Progress`
- Upon approval: `Feature, High Priority, Improvement, in review`

**Merging:**
- Pipeline tests passed successfully.
- Code reviewed and approved by @ravisha.

**Reviewer Duties:**
- Reviewed the code for quality standards.
- Provided constructive feedback on the implementation.
- Approved the PR for merging.

## Contact Info

If you have any questions, need further assistance or wish to contribute, please reach out to:

- **Project Lead :** Ravi (Email: gusravish@student.gu.se)

Thank you for considering contributing to our project!