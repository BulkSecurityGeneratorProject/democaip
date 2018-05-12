import { BaseEntity, User } from './../../shared';

export class Project implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public creation?: any,
        public epics?: BaseEntity[],
        public owner?: User,
    ) {
    }
}
