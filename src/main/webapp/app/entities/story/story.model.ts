import { BaseEntity, User } from './../../shared';

export class Story implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public creation?: any,
        public storyPoint?: number,
        public tasks?: BaseEntity[],
        public owner?: User,
        public epic?: BaseEntity,
    ) {
    }
}
