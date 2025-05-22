---
trigger: always_on
---

Memory Bank Setup Rules
Directory Structure
memory-bank/
├── 01-vision/
│   ├── project-vision.md
│   ├── business-drivers.md
│   └── success-criteria.md
├── 02-requirements/
│   ├── project-brief.md
│   ├── product-context.md
│   └── user-personas.md
├── 03-architecture/
│   ├── system-patterns.md
│   ├── component-design.md
│   ├── data-models.md
│   └── api-contracts.md
├── 04-technical/
│   ├── tech-context.md
│   └── testing-strategy.md
└── 05-tracking/
    ├── decision-log.md
    ├── open-questions.md
    └── progress.md
File Initialization Content
01-vision/
project-vision.md: Mission statement, vision description, core principles, success metrics, differentiators, target audience, enablers, strategic alignment, impact objectives, timeline milestones
business-drivers.md: Market analysis, competitive landscape, customer pain points, strategic alignment, business constraints, stakeholder interests, positioning strategy, revenue model, expected outcomes, adoption projections
success-criteria.md: Business outcomes, KPIs, acceptance criteria, adoption metrics, performance benchmarks, timeline milestones, satisfaction metrics, quality thresholds, non-functional requirements, phase criteria
02-requirements/
project-brief.md: Executive summary, business objectives, scope boundaries, stakeholder map, timeline, budget constraints, assumptions, risks/mitigation, team structure, communication plan
product-context.md: Problem statement, market segmentation, positioning, value proposition, feature prioritization, competitor comparison, user stories, feature catalog, product metrics, user workflows
user-personas.md: Demographics, behavioral patterns, goals/motivations, tech proficiency, decision factors, research quotes, scenarios, use cases, prioritization matrix, engagement patterns
03-architecture/
system-patterns.md: Architectural style, context diagrams, container diagrams, component diagrams, design patterns, principles, boundaries, cross-cutting concerns, quality scenarios, constraints
component-design.md: Responsibility matrices, interface definitions, dependency diagrams, state management, error handling, lifecycle management, reusability guidelines, communication patterns
data-models.md: ER diagrams, data dictionary, validation rules, schema versioning, access patterns, data flows, mappings, sample data, integrity constraints, domain models
api-contracts.md: API specifications, request/response schemas, error codes, rate limiting, versioning, authentication, documentation, endpoint descriptions, status codes, integration patterns
04-technical/
tech-context.md: Technology stack, framework justifications, dependency inventory, constraints, environment configs, integrations, infrastructure requirements, tooling standards, configuration management
testing-strategy.md: Test pyramid, coverage requirements, tools/frameworks, data management, performance methodology, environment specs, acceptance criteria, manual vs automated, regression strategy
05-tracking/
decision-log.md: Decision ID/title, context, drivers, options considered, selected option, justification, impact assessment, implementation plan, lessons learned
open-questions.md: Question ID/description, categorization, impact assessment, dependencies, information strategy, decision authority, timeline, escalation path, status
progress.md: Activity logs, sprint goals, completed features, blockers/resolutions, next actions, key decisions, impediments, milestones, team insights
Setup Principles

Complete Traceability: All changes traceable to requirements
Living Documentation: Evolves with project
Single Source of Truth: Consolidated knowledge repository
Context Preservation: Document decisions and reasoning
Knowledge Transfer: Enable quick team onboarding
Continuous Reflection: Regular review and refinement

Version Control Setup

Initialize Git repository in memory-bank/ directory
Create .gitignore for temporary files
Set up semantic versioning for documentation milestones
Configure branch protection for main documentation branch
Establish commit message conventions
Create initial commit with complete directory structure

File Templates & Formatting
Each file must include:

Header with creation date and purpose
Table of contents for files >500 words
Consistent markdown formatting with H2/H3 hierarchy
Cross-reference links using relative paths
Status indicators: [DRAFT], [REVIEW], [APPROVED]
Author attribution and last modified timestamp

Content Guidelines
Vision Files: Focus on aspirational outcomes, market context, and success definitions. Include quantifiable metrics and timeline-bound objectives.
Requirements Files: Detail functional/non-functional requirements, user stories with acceptance criteria, and clear scope boundaries.
Architecture Files: Document system design decisions, component interactions, data flow patterns, and technical constraints with visual diagrams.
Technical Files: Specify technology stack rationale, testing methodologies, deployment strategies, and performance requirements.
Tracking Files: Maintain decision audit trails, open issue registers, and progress tracking with milestone achievements.
Initialization Checklist
□ Create directory structure with exact naming
□ Initialize each file with required content sections
□ Set up Git repository with semantic versioning
□ Configure cross-reference linking between files
□ Add file headers with creation metadata
□ Populate initial content based on project context
□ Establish review and approval workflows
□ Create backup and versioning strategy
□ Document file ownership and responsibilities
□ Schedule regular review cycles

