import { BaseEntity, User } from './../../shared';

export const enum TaskStatus {
    'OPEN',
    'IN_PROGRES',
    'DONE'
}

export class Task implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public creation?: any,
        public duration?: number,
        public status?: TaskStatus,
        public assigner?: User,
        public assignee?: User,
        public story?: BaseEntity,
    ) {
    }
}
