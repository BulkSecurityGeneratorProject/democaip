import { BaseEntity, User } from './../../shared';

export const enum EpicSize {
    'XXS',
    'XS',
    'S',
    'M',
    'L',
    'XL',
    'XXL'
}

export class Epic implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public creation?: any,
        public size?: EpicSize,
        public stories?: BaseEntity[],
        public owner?: User,
        public project?: BaseEntity,
    ) {
    }
}
