export const enum SecureConnection {
    NONE = 'NONE',
    SSL = 'SSL',
    TLS = 'TLS'
}

export interface INotificator {
    id?: number;
    smtpHost?: string;
    smtpPort?: number;
    smtpAuth?: boolean;
    username?: string;
    password?: string;
    secureConnection?: SecureConnection;
    companyId?: number;
}

export class Notificator implements INotificator {
    constructor(
        public id?: number,
        public smtpHost?: string,
        public smtpPort?: number,
        public smtpAuth?: boolean,
        public username?: string,
        public password?: string,
        public secureConnection?: SecureConnection,
        public companyId?: number
    ) {
        this.smtpAuth = this.smtpAuth || false;
    }
}
